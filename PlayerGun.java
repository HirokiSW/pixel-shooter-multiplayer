/**
    This class represents the two guns of a player, both primary and secondary.
    The class handles the animation logic of the player's current weapon,
    and facilitates logic for shooting, reloading, and switching between the two guns.
    It updates the gun states depending on currentTime, creates ProjectileBullet
    instances, and reflects the recoil and bobbing effect of the player.

    @author Hiroki S. Watanabe (244844)
    @author Yuuki S. Watanabe (244845)
    @version 20 May 2025
    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.
    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.
    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
**/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class PlayerGun implements SpriteGraphic {
    private ShapeDimensions spriteBounds;
    private Player body;
    private double[][] gunStats;
    private int[] gunNum;
    private double currentTime, timeSinceLastShot, timeSinceReload, timeSinceSwitch, recoilIncrement;
    private int currGun, currSeedIndex;
    private boolean switching, reloading, shooting;
    private ArrayList<ObjectProjectile> projectiles;
    private ArrayList<SpriteGraphic> projectileGraphics;

    /**
     * Constructs a PlayerGun object for the given player, assigning them two guns.
     * The lists for projectiles are also passed here for the generation of ProjectileBullet instances.
     * A list of gun stats is created for easy access of gun values from Const.
     *
     * @param body the player owning the gun
     * @param gun1ID the ID of the primary gun
     * @param gun2ID the ID of the secondary gun
     * @param projectiles the list for game projectile hitboxes
     * @param projectileGraphics the list for graphic representations of projectiles
     */
    public PlayerGun(Player body, int gun1ID, int gun2ID, ArrayList<ObjectProjectile> projectiles, ArrayList<SpriteGraphic> projectileGraphics) {
        this.body = body;
        this.projectiles = projectiles;
        this.projectileGraphics = projectileGraphics;
        
        gunNum = new int[2];
        gunNum[Const.PRIMARY] = gun1ID;
        gunNum[Const.SECONDARY] = gun2ID;

        gunStats = new double[2][Const.BULLETS_LEFT + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < Const.BULLETS_LEFT; j++) {
                gunStats[i][j] = Const.GUN_STATS[gunNum[i]][j];
            }
            gunStats[i][Const.BULLETS_LEFT] = Const.GUN_STATS[gunNum[i]][Const.BULLET_CAPACITY];
        }
        body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]);
        spriteBounds = new ShapeDimensions(
            body.getImage(Const.X) + body.getImage(Const.WIDTH)/2 - Const.GUN_STATS[gunNum[currGun]][Const.HANDLE_WIDTH]/2, 
            body.getImage(Const.Y) + body.getImage(Const.HEIGHT)/2 + Const.GUN_STATS[gunNum[currGun]][Const.HANDLE_HEIGHT]/1.5, 
            Const.GUN_STATS[gunNum[currGun]][Const.HANDLE_WIDTH] + Const.GUN_STATS[gunNum[currGun]][Const.BARREL_LENGTH], 
            Const.GUN_STATS[gunNum[currGun]][Const.HANDLE_HEIGHT] + Const.GUN_STATS[gunNum[currGun]][Const.BARREL_HEIGHT]
        );
    }

    @Override
    public void draw(Graphics2D g2d, SpriteManager sm) {
        if (body.isDead()) return;

        AffineTransform revert = g2d.getTransform();
        double degRotation = Math.toDegrees(body.getImage(Const.ROTATION));
        g2d.setColor(new Color(255, 255, 255, 200));

        if (!isSwitching()) {
            if (isReloading()) {
                drawReloadingBar(degRotation, g2d);
            }
            sm.drawGunSprite(g2d, getCurrentGun(), spriteBounds);
        } else {
            drawSwitchingBar(degRotation, g2d);
        }
        g2d.setTransform(revert);
    }

    /**
     * Draws the reloading progress bar beside the player.
     *
     * @param degRotation the current rotation angle of the player
     * @param g2d the Graphics2D object to draw on
     */
    private void drawReloadingBar(double degRotation, Graphics2D g2d) {
        if ((degRotation % 360 > 90 && degRotation % 360 < 270) || (degRotation % 360 < -90 && degRotation % 360 > -270)) {
            spriteBounds.setRotation(Math.toRadians(145));
            g2d.fillRect((int) body.getImage(Const.X) + 56, (int) body.getImage(Const.Y) + 25, 5, (int) (45*getReloadProgress()));
        } else {
            spriteBounds.setRotation(Math.toRadians(35));
            g2d.fillRect((int) body.getImage(Const.X) - 15, (int) body.getImage(Const.Y) + 25, 5, (int) (45*getReloadProgress()));
        }
    }

    /**
     * Draws the weapon-switching progress bar beside the player.
     *
     * @param degRotation the current rotation angle of the player
     * @param g2d the Graphics2D object to draw on
     */
    private void drawSwitchingBar(double degRotation, Graphics2D g2d) {
        if ((degRotation % 360 > 90 && degRotation % 360 < 270) || (degRotation % 360 < -90 && degRotation % 360 > -270)) {
            g2d.fillRect((int) body.getImage(Const.X) + 56, (int) body.getImage(Const.Y) + 25, 5, (int) (45*getSwitchingProgress()));
        } else {
            g2d.fillRect((int) body.getImage(Const.X) - 15, (int) body.getImage(Const.Y) + 25, 5, (int) (45*getSwitchingProgress()));
        }
    }

    /**
     * Updates the state of the gun, handling reloading, 
     * switching, recoil recovery, and firing condiitions.
     * currentTime is continuously incremented for time basis.
     */
    public void update() {
        if (body.isDead()) return;

        adjustSpriteTransformations();

        currentTime += Const.MILISECONDS_PER_FRAME;
        boolean finishedReloading = currentTime - timeSinceReload >= gunStats[currGun][Const.RELOAD_TIME] && reloading;
        boolean finishedRecovering = currentTime - timeSinceLastShot >= gunStats[currGun][Const.SHOOT_INTERVAL] && shooting && gunStats[currGun][Const.BULLETS_LEFT] > 0 && !reloading && !switching && !body.isDodging();
        boolean finishedSwitching = currentTime - timeSinceSwitch >= gunStats[currGun][Const.SWITCH_TIME] && switching;
        boolean stillRecovering = recoilIncrement > 0 && currentTime - timeSinceLastShot <= gunStats[currGun][Const.SHOOT_INTERVAL];

        if (finishedReloading) gunReloaded();
        if (finishedRecovering) gunShot();
        if (stillRecovering) recoilIncrement = (gunStats[currGun][Const.RECOIL]/Math.pow(gunStats[currGun][Const.SHOOT_INTERVAL], 2)) * Math.pow((currentTime - timeSinceLastShot - gunStats[currGun][Const.SHOOT_INTERVAL]), 2);
        if (finishedSwitching) gunSwitched();
    }

    /**
     * Adjusts the gun's position, rotation, and pivot based on player's mouse and position.
     */
    private void adjustSpriteTransformations() {
        double centerX = (
            Const.FRAME_WIDTH/2 - (body.getMouse(Const.X) - 
            Const.FRAME_WIDTH/2)*getGunStat(Const.VISION_RANGE) + 
            (body.getCameraPerspective(Const.X) - body.getTargetPerspective(Const.X))
        );
        double centerY = (
            Const.FRAME_HEIGHT/2 + Const.GUN_STATS[getCurrentGun()][Const.HANDLE_HEIGHT]/1.5 + Const.GUN_STATS[getCurrentGun()][Const.BARREL_HEIGHT] - 
            (body.getMouse(Const.Y) - Const.FRAME_HEIGHT/2)*body.getGun().getGunStat(Const.VISION_RANGE) + 
            (body.getCameraPerspective(Const.Y) - body.getTargetPerspective(Const.Y))
        );
        spriteBounds.setX(
            (body.getImage(Const.X) + body.getImage(Const.WIDTH)/2 - 
            Const.GUN_STATS[getCurrentGun()][Const.HANDLE_WIDTH]/2) - recoilIncrement
        );
        spriteBounds.setY(
            (body.getImage(Const.Y) + body.getImage(Const.HEIGHT)/2 + 
            Const.GUN_STATS[getCurrentGun()][Const.HANDLE_HEIGHT]/1.5) + body.getBobbingIncrement()
        );
        spriteBounds.setRotation(Math.atan2(
            body.getMouse(Const.Y) - centerY, 
            body.getMouse(Const.X) - centerX
        ));
        spriteBounds.setPivot(
            body.getImage(Const.X) + body.getImage(Const.WIDTH)/2, 
            getImage(Const.Y) + Const.GUN_STATS[getCurrentGun()][Const.BARREL_HEIGHT]
        );
    }

    /**
     * Sets whether the gun is currently switching.
     *
     * @param b true to start switching, false to stop
     */
    public void setSwitching(boolean b) {
        switching = b && !body.isDodging();
        if (switching) {
            setReloading(false);
            timeSinceSwitch = currentTime;
            body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]*0.2);
        } else if (!body.isDodging()) {
            body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]);
        }
    }

    /**
     * Sets whether the gun is currently reloading.
     *
     * @param b true to start reloading, false to stop
     */
    public void setReloading(boolean b) {
        reloading = b && !body.isDodging();
        if (reloading) {
            setSwitching(false);
            timeSinceReload = currentTime;
            body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]*0.5);
        } else if (!body.isDodging()) {
            body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]);
        }
    }

    /**
     * Sets whether the gun is currently firing.
     *
     * @param b true to start shooting, false to stop
     */
    public void setShooting(boolean b) {
        shooting = b;
        if (shooting) {
            setSwitching(false);
            setReloading(false);
        }
    }

    /**
     * Handles completion of a gun switch, updating visuals and gun index.
     */
    public void gunSwitched() {
        currGun = (currGun + 1) % 2;
        spriteBounds.setWidth(Const.GUN_STATS[getCurrentGun()][Const.HANDLE_WIDTH] + Const.GUN_STATS[getCurrentGun()][Const.BARREL_LENGTH]);
        spriteBounds.setHeight(Const.GUN_STATS[getCurrentGun()][Const.HANDLE_HEIGHT] + Const.GUN_STATS[getCurrentGun()][Const.BARREL_HEIGHT]);
        recoilIncrement = 0;
        setSwitching(false);
    }

    /**
     * Handles completion of a reload, resetting bullet count.
     */
    public void gunReloaded() {
        gunStats[currGun][Const.BULLETS_LEFT] = gunStats[currGun][Const.BULLET_CAPACITY];
        setReloading(false);
    }

    /**
     * Handles shooting logic, including bullet generation, recoil, and firing conditions.
     */
    public void gunShot() {
        for (int i = 0; i < Const.GUN_STATS[gunNum[currGun]][Const.BULLET_COUNT]; i++) {
            ObjectProjectile bullet = new ProjectileBullet(body, this, Const.RANDOM_SEED[currSeedIndex], i == 0);
            currSeedIndex = (currSeedIndex + 1) % 100;
            projectiles.add(bullet);
            projectileGraphics.add((SpriteGraphic) bullet);
        }
        body.triggerShake(gunStats[currGun][Const.RECOIL], 100);
        gunStats[currGun][Const.BULLETS_LEFT]--;
        recoilIncrement = gunStats[currGun][Const.RECOIL];
        timeSinceLastShot = currentTime;
    }

    /**
     * Changes the primary or secondary gun slot 
     * assigned to player to the respective chosen gun.
     *
     * @param gunIndex the gun slot to change, primary(0) or secondary(1)
     * @param id the new gun ID to assign
     */
    public void toggleGun(int gunIndex, int id) {
        gunNum[gunIndex] = id;
        for (int j = 0; j < Const.BULLETS_LEFT; j++) {
            gunStats[gunIndex][j] = Const.GUN_STATS[gunNum[gunIndex]][j];
        }
        spriteBounds.setWidth(Const.GUN_STATS[getCurrentGun()][Const.HANDLE_WIDTH] + Const.GUN_STATS[getCurrentGun()][Const.BARREL_LENGTH]);
        spriteBounds.setHeight(Const.GUN_STATS[getCurrentGun()][Const.HANDLE_HEIGHT] + Const.GUN_STATS[getCurrentGun()][Const.BARREL_HEIGHT]);
        gunStats[gunIndex][Const.BULLETS_LEFT] = Const.GUN_STATS[gunNum[gunIndex]][Const.BULLET_CAPACITY];
        body.changeSpeed(gunStats[currGun][Const.MOVE_SPEED]);
    }

    /**
     * Gets the slot of the currently equipped gun, primary(0) or secondary(1).
     *
     * @return current gun index
     */
    public int getCurrGunNum() {
        return currGun;
    }

    /**
     * Gets the gun ID of the currently equipped gun.
     *
     * @return current gun ID
     */
    public int getCurrentGun() {
        return gunNum[currGun];
    }

    /**
     * Gets the gun ID of the specified gun slot.
     *
     * @param gunIndex the specified gun slot, primary(0) or secondary(1)
     * @return gun ID at specified slot
     */
    public int getGun(int gunIndex) {
        return gunNum[gunIndex];
    }

    /**
     * Gets a stat value of the current gun.
     *
     * @param statNum the stat index (as defined in Const)
     * @return stat value
     */
    public double getGunStat(int statNum) {
        return gunStats[currGun][statNum];
    }

    /**
     * Gets a stat value of a specific gun slot.
     *
     * @param gunIndex the specified gun slot, primary(0) or secondary(1)
     * @param statNum the stat index (as defined in Const)
     * @return stat value
     */
    public double getGunStat(int gunIndex, int statNum) {
        return gunStats[gunIndex][statNum];
    }

    /**
     * Gets the reload progress ratio from 0 to 1.
     *
     * @return reload progress
     */
    public double getReloadProgress() {
        return Math.clamp((currentTime - timeSinceReload) / getGunStat(Const.RELOAD_TIME), 0, 1);
    }

    /**
     * Gets the weapon-switching progress ratio from 0 to 1.
     *
     * @return switching progress
     */
    public double getSwitchingProgress() {
        return Math.clamp((currentTime - timeSinceSwitch) / getGunStat(Const.SWITCH_TIME), 0, 1);
    }

    /**
     * Checks if the gun is currently reloading.
     *
     * @return true if reloading, else false
     */
    public boolean isReloading() {
        return reloading;
    }

    /**
     * Checks if the gun is currently switching.
     *
     * @return true if switching, else false
     */
    public boolean isSwitching() {
        return switching;
    }

    /**
     * Checks if the gun is currently shooting.
     *
     * @return true if shooting, else false
     */
    public boolean isShooting() {
        return shooting;
    }

    /**
     * Gets the current recoil offset applied to the gun.
     *
     * @return recoil increment
     */
    public double getRecoilIncrement() {
        return recoilIncrement;
    }

    @Override
    public double getImage(int dimensionID) {
        switch (dimensionID) {
            case Const.X: return spriteBounds.getX();
            case Const.Y: return spriteBounds.getY();
            case Const.WIDTH: return spriteBounds.getWidth();
            case Const.HEIGHT: return spriteBounds.getHeight();
            case Const.ROTATION: return spriteBounds.getRotation();
            case Const.PIVOT_X: return spriteBounds.getPivotX();
            case Const.PIVOT_Y: return spriteBounds.getPivotY();
            default: return 0;
        }
    }
    @Override
    public int getLayer() {
        return body.getLayer() + 1;
    }
}
