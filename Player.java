/**
    This class represents the player itself, its visual representation and its interaction with the game.
    The class handles the animation logic of the player's different states,
    and facilitates logic for collision, camera perspective, camera shake, and resurrection timer.
    The gun, stats, controller, and input reader of the player is also instantiated here.

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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Player implements ObjectEntity, SpriteGraphic {
    private ShapeDimensions hitbox;
    private ShapeDimensions spriteBounds;
    private PlayerGun gun;
    private PlayerStats stats;
    private PlayerController pc;
    private PlayerInputReader pi;

    private int characterID, playerID, currAnimation, frameIndex, currSeedIndex;
    private double timeSinceDodge, dodgeDuration, hp, baseSpeed, camSpeed, shakeDuration, shakeMagnitude, deathTimer;
    private double[] targetCam, cam, camShake;
    private boolean knocked, inPVP, dead;

    private double[] currFrame, gap, speed, mouse, knockback;
    private boolean[] willCollide, isColliding, isMoving, insideTeleporter;
    
    /**
     * Constructs a new Player object with a given ID, character design, two guns, and projectile lists.
     *
     * @param playerID the ID associated with this player
     * @param characterID the character model/type to use
     * @param gun1ID the ID of the primary gun
     * @param gun2ID the ID of the secondary gun
     * @param projectiles the list for game projectile hitboxes
     * @param projectileGraphics the list for graphic representations of projectiles
     */
    public Player(int playerID, int characterID, int gun1ID, int gun2ID, ArrayList<ObjectProjectile> projectiles, ArrayList<SpriteGraphic> projectileGraphics) {
        this.playerID = playerID;
        this.characterID = characterID;

        hp = Const.CHARACTER_HP;
        camSpeed = 0.15;
        currFrame = new double[3];
        gap = new double[4];
        speed = new double[2];
        mouse = new double[2];
        knockback = new double[2];
        targetCam = new double[2];
        cam = new double[2];
        camShake = new double[2];

        willCollide = new boolean[4];
        isColliding = new boolean[4];
        isMoving = new boolean[4];
        insideTeleporter = new boolean[Const.NUMBER_OF_TELEPORTERS];

        spriteBounds = new ShapeDimensions(
            500 - Const.CHARACTER_WIDTH/2 - (playerID)*(Const.CHARACTER_WIDTH*1.5), 
            2330 - Const.CHARACTER_HEIGHT/2, 
            Const.CHARACTER_WIDTH, 
            Const.CHARACTER_HEIGHT
        );
        hitbox = new ShapeDimensions(
            getImage(Const.X) + 5, 
            getImage(Const.Y) + 40, 
            Const.CHARACTER_WIDTH - 10, 
            Const.CHARACTER_HEIGHT - 40
        );
        
        gun = new PlayerGun(this, gun1ID, gun2ID, projectiles, projectileGraphics);
        stats = new PlayerStats(this);
    }

    @Override
    public void draw(Graphics2D g2d, SpriteManager sm) {
        if (dead) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        
        if (isDodging()) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            sm.drawAnimatedEntity(g2d, Const.IDLE_ANIM[characterID], spriteBounds, 1);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            return;
        }

        if (!knocked) {
            setToMovingAnimation();
        } else {
            setToKnockedAnimation();
        }
        sm.drawAnimatedEntity(g2d, currAnimation, spriteBounds, (int) currFrame[frameIndex]);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Sets the player's animation to either idle or running depending on movement state.
     * Not used when the player is currently in a knocked-back state.
     */
    private void setToMovingAnimation() {
        if (isCurrentlyMoving()) {
            currAnimation = Const.RUN_ANIM[characterID];
            frameIndex = 1;
        } else {
            currAnimation = Const.IDLE_ANIM[characterID];
            frameIndex = 0;
        }
        currFrame[frameIndex] = (currFrame[frameIndex]  + Const.ANIM_SPEED[currAnimation]) % Const.ANIM_FRAMES[currAnimation];
    }

    /**
     * Sets the player's animation to the "hit" state.
     * Resets the animation and ends knockback when frames are completed.
     */
    private void setToKnockedAnimation() {
        currAnimation = Const.HIT_ANIM[characterID];
        frameIndex = 2;
        currFrame[frameIndex] = (currFrame[frameIndex]  + Const.ANIM_SPEED[currAnimation]);
        if (currFrame[frameIndex] >= Const.ANIM_FRAMES[currAnimation]) {
            currFrame[frameIndex] = 0;
            endKnockback();
        }
    }

    @Override
    public void updatePosition() {
        updateDodgeConditions();
        updateReviveConditions();
        updateKnockbackConditions();

        if (dead) return;

        updateSpeed();
        if (isMoving[Const.UP] && !willCollide[Const.UP]) moveY(-speed[Const.Y]);
        else if (isMoving[Const.UP] && !isColliding[Const.UP]) moveY(gap[Const.UP]);
        if (isMoving[Const.DOWN] && !willCollide[Const.DOWN]) moveY(speed[Const.Y]);
        else if (isMoving[Const.DOWN] && !isColliding[Const.DOWN]) moveY(-gap[Const.DOWN]);
        if (isMoving[Const.LEFT] && !willCollide[Const.LEFT]) moveX(-speed[Const.X]);
        else if (isMoving[Const.LEFT] && !isColliding[Const.LEFT]) moveX(gap[Const.LEFT]);
        if (isMoving[Const.RIGHT] && !willCollide[Const.RIGHT]) moveX(speed[Const.X]);
        else if (isMoving[Const.RIGHT] && !isColliding[Const.RIGHT]) moveX(-gap[Const.RIGHT]);
        
        adjustCamera();
        adjustSpriteTransformations();

        resetCollisions();
    }

    /**
     * Updates conditions related to dodging, such as decrementing dodge duration and checking if dodge should end.
     */
    private void updateDodgeConditions() {
        dodgeDuration -= Const.MILISECONDS_PER_FRAME;
        timeSinceDodge += Const.MILISECONDS_PER_FRAME;
        if ((int) dodgeDuration == 0) {
            setDodging(false);
        }
    }

    /**
     * Updates conditions related to player's death.
     * Player is revived by checking the death timer and restoring health when enough time has passed.
     */
    private void updateReviveConditions() {
        if (isDead()) deathTimer += Const.MILISECONDS_PER_FRAME;
        else deathTimer = 0;
        if (deathTimer > Const.RESPAWN_TIME) {
            setHealth(Const.CHARACTER_HP);
            deathTimer = 0;
        }
    }

    /**
     * Applies knockback displacement if applicable and resets knockback values after movement.
     */
    private void updateKnockbackConditions() {
        if (knockback[Const.X] < 0) {
            if (!willCollide[Const.LEFT]) moveX(knockback[Const.X]);
        } else {
            if (!willCollide[Const.RIGHT]) moveX(knockback[Const.X]);
        }
        knockback[Const.X] = 0;
        if (knockback[Const.Y] < 0) {
            if (!willCollide[Const.UP]) moveY(knockback[Const.Y]);
        } else {
            if (!willCollide[Const.DOWN]) moveY(knockback[Const.Y]);
        }
        knockback[Const.Y] = 0;
    }

    /**
     * Calculates movement speed for the player. If moving diagonally, reduces speed accordingly.
     */
    private void updateSpeed() {
        if ((isMoving[Const.UP] ^ isMoving[Const.DOWN]) && (isMoving[Const.LEFT] ^ isMoving[Const.RIGHT])) {
            speed[Const.X] = baseSpeed*Math.sqrt(2)/2;
            speed[Const.Y] = baseSpeed*Math.sqrt(2)/2;
        } else {
            speed[Const.X] = baseSpeed;
            speed[Const.Y] = baseSpeed;
        }
    }

    /**
     * Updates the player's camera based on the current position and gun vision.
     * Camera shake is applied when a value is present.
     */
    private void adjustCamera() {
        targetCam[Const.X] = (
            Const.FRAME_WIDTH/2 - getImage(Const.WIDTH)/2 - getImage(Const.X) - 
            (getMouse(Const.X) - Const.FRAME_WIDTH/2)*getGun().getGunStat(Const.VISION_RANGE)
        );
        targetCam[Const.Y] = (
            Const.FRAME_HEIGHT/2 - getImage(Const.HEIGHT)/2 - getImage(Const.Y) - 
            (getMouse(Const.Y) - Const.FRAME_HEIGHT/2)*getGun().getGunStat(Const.VISION_RANGE)
        );
        cam[Const.X] += (targetCam[Const.X] - cam[Const.X])*camSpeed;
        cam[Const.Y] += (targetCam[Const.Y] - cam[Const.Y])*camSpeed;
        if (shakeDuration > 0) {
            camShake[Const.X] = (Const.RANDOM_SEED[currSeedIndex] - 0.5)*shakeMagnitude;
            currSeedIndex = (currSeedIndex + 1) % 100;
            camShake[Const.Y] = (Const.RANDOM_SEED[currSeedIndex] - 0.5)*shakeMagnitude;
            currSeedIndex = (currSeedIndex + 1) % 100;
            shakeDuration -= Const.MILISECONDS_PER_FRAME;
        } else {
            camShake[Const.X] = 0;
            camShake[Const.Y] = 0;
        }
    }

    /**
     * Applies sprite transformations such as rotation and pivoting based on current mouse position and camera perspective.
     */
    private void adjustSpriteTransformations() {
        spriteBounds.setPivot(getImage(Const.X) + getImage(Const.WIDTH)/2, getImage(Const.Y) + getImage(Const.HEIGHT)/2);
        spriteBounds.setRotation(Math.atan2(
            mouse[Const.Y] - Const.FRAME_HEIGHT/2 - (getCameraPerspective(Const.Y) - getTargetPerspective(Const.Y)), 
            mouse[Const.X] - Const.FRAME_WIDTH/2 - (getCameraPerspective(Const.X) - getTargetPerspective(Const.X))
        ));
    }

    /**
     * Resets all collision variables.
     */
    private void resetCollisions() {
        for (int i = 0; i < 4; i++) {
            gap[i] = 0;
            willCollide[i] = false;
            isColliding[i] = false;
        }
    }

    @Override
    public void detectCollision(ObjectHitbox o) {
        if (dead) return;

        double left = getHitbox(Const.X);
        double right = left + getHitbox(Const.WIDTH);
        double top = getHitbox(Const.Y);
        double bottom = top + getHitbox(Const.HEIGHT);
        double speedX = getSpeed(Const.X);
        double speedY = getSpeed(Const.Y);

        double otherLeft = o.getHitbox(Const.X);
        double otherRight = otherLeft + o.getHitbox(Const.WIDTH);
        double otherTop = o.getHitbox(Const.Y);
        double otherBottom = otherTop + o.getHitbox(Const.HEIGHT);

        boolean withinY = !(top >= otherBottom || bottom <= otherTop);
        boolean withinX = !(left >= otherRight || right <= otherLeft);

        if (withinY) {
            if (!willCollide[Const.LEFT]) {
                willCollide[Const.LEFT] = (left - speedX < otherRight) && (right > otherRight);
                gap[Const.LEFT] = otherRight - left;
            }
            if (!willCollide[Const.RIGHT]) {
                willCollide[Const.RIGHT] = (right + speedX > otherLeft) && (left < otherLeft);
                gap[Const.RIGHT] = right - otherLeft;
            }
            if (!isColliding[Const.LEFT]) isColliding[Const.LEFT] = (left < otherRight) && (right > otherRight);
            if (!isColliding[Const.RIGHT]) isColliding[Const.RIGHT] = (right > otherLeft) && (left < otherLeft);
        }

        if (withinX) {
            if (!willCollide[Const.UP]) {
                willCollide[Const.UP] = (top - speedY < otherBottom) && (bottom > otherBottom);
                gap[Const.UP] = otherBottom - top;
            }
            if (!willCollide[Const.DOWN]) {
                willCollide[Const.DOWN] = (bottom + speedY > otherTop) && (top < otherTop);
                gap[Const.DOWN] = bottom - otherTop;
            }
            if (!isColliding[Const.UP]) isColliding[Const.UP] = (top < otherBottom) && (bottom > otherBottom);
            if (!isColliding[Const.DOWN]) isColliding[Const.DOWN] = (bottom > otherTop) && (top < otherTop);
        }
    }

    /**
     * Starts movement in the given direction.
     * @param direction the direction to move in based on Const direction IDs.
     */
    public void moving(int direction) {
        switch(direction) {
            case Const.UP: isMoving[Const.UP] = true; break;
            case Const.DOWN: isMoving[Const.DOWN] = true; break;
            case Const.LEFT: isMoving[Const.LEFT] = true; break;
            case Const.RIGHT: isMoving[Const.RIGHT] = true; break;
        }
    }
    
    /**
     * Stops movement in the given direction.
     * @param direction the direction to stop moving in based on Const direction IDs.
     */
    public void stopMoving(int direction) {
        switch(direction) {
            case Const.UP: isMoving[Const.UP] = false; break;
            case Const.DOWN: isMoving[Const.DOWN] = false; break;
            case Const.LEFT: isMoving[Const.LEFT] = false; break;
            case Const.RIGHT: isMoving[Const.RIGHT] = false; break;
        }
    }

    /**
     * Changes the current character model.
     * @param id the ID of the character to switch to.
     */
    public void toggleCharacter(int id) {
        characterID = id;
    }

    /**
     * Changes the base movement speed of the player.
     * @param s the new base speed.
     */
    public void changeSpeed(double s) {
        baseSpeed = s;
    }

    /**
     * Updates the mouse position relative to the player's view.
     * @param x the x-coordinate of the mouse.
     * @param y the y-coordinate of the mouse.
     */
    public void setMouse(double x, double y) {
        mouse[Const.X] = x;
        mouse[Const.Y] = y;
    }

    /**
     * Creates the PlayerController object for client-side input generation.
     * @param wts the WriteToServer object for sending data to the server.
     */
    public void createController(WriteToServer wts) {
        pc = new PlayerController(this, wts);
    }

    /**
     * Initializes a simulated controller for reading input from server data.
     * @param rfs the ReadFromServer object for receiving data.
     */
    public void simulateController(ReadFromServer rfs) {
        pi = new PlayerInputReader(this);
    }

    /**
     * Triggers a camera shake effect.
     * @param magnitude the strength of the shake.
     * @param duration the length of time the shake lasts (in miliseconds).
     */
    public void triggerShake(double magnitude, double duration) {
        shakeDuration = duration;
        shakeMagnitude = magnitude/2;
    }

    /**
     * Sets whether the player is currently inside a specific teleporter.
     * @param id the teleporter ID
     * @param b true if inside, else false
     */
    public void setInTeleporter(int id, boolean b) {
        insideTeleporter[id] = b;
    }

    /**
     * Enables or disables PVP mode for the player.
     * @param b true to enable PVP mode, false to disable.
     */
    public void setPVP(boolean b) {
        inPVP = b;
    }

    /**
     * Enables or disables the dodge state. 
     * Initiates a dodge if conditions allow.
     * @param b true to initiate dodge, false to end dodge state.
     */
    public void setDodging(boolean b) {
        if (b && timeSinceDodge > Const.DASH_COOLDOWN && !gun.isReloading() && !gun.isSwitching() && !knocked && !dead) {
            dodgeDuration = Const.DASH_DURATION;
            timeSinceDodge = 0;
            changeSpeed(gun.getGunStat(Const.MOVE_SPEED)*2.5);
        } else if (!b) {
            changeSpeed(gun.getGunStat(Const.MOVE_SPEED));
        }
    }

    /**
     * Smoothly and slowly corrects the player's position towards target position dictated by server data.
     * If distance between positions is over 500, teleportation is assumed.
     * @param targetX target x-coordinate.
     * @param targetY target y-coordinate.
     */
    public void adjustPosition(double targetX, double targetY) {
        double xDiff = targetX - spriteBounds.getX();
        double yDiff = targetY - spriteBounds.getY();
        if (Math.sqrt(xDiff*xDiff + yDiff*yDiff) > 500) return;
        moveX(xDiff*0.05);
        moveY(yDiff*0.05);
    }

    /**
     * Checks whether the player is currently dodging.
     * @return true if dodging, else false.
     */
    public boolean isDodging() {
        return dodgeDuration > 0;
    }

    /**
     * Checks whether the player is currently in PVP mode.
     * @return true if in PVP, else false.
     */
    public boolean isInPVP() {
        return inPVP;
    }

    /**
     * Returns ID unique to each player.
     * @return the player ID.
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Checks if the player is currently inside a specific teleporter.
     * @param id the teleporter ID.
     * @return true if inside, else false.
     */
    public boolean isCurrentlyInTeleporter(int id) {
        return insideTeleporter[id];
    }

    /**
     * Checks whether the player is currently moving in any direction.
     * Opposite directions cancel each other out.
     * @return true if moving, else false.
     */
    public boolean isCurrentlyMoving() {
        return (isMoving[Const.UP] ^ isMoving[Const.DOWN]) || (isMoving[Const.LEFT] ^ isMoving[Const.RIGHT]);
    }

    /**
     * Returns the current mouse position on the given dimension.
     * @param dimensionID specified dimension ID as stated in Const.
     * @return the mouse position value for that axis.
     */
    public double getMouse(int dimensionID) {
        return mouse[dimensionID];
    }

    
    /**
     * Returns the increment value used for head bobbing animation based on player movement frame.
     * @return the bobbing animation offset value.
     */
    public double getBobbingIncrement() {
        if (!isCurrentlyMoving()) {
            return ((currFrame[0] - Const.ANIM_SPEED[Const.IDLE_ANIM[characterID]]) % Const.ANIM_FRAMES[Const.IDLE_ANIM[characterID]])*0.7;
        } else {
            return ((currFrame[1] - baseSpeed/30) % Const.ANIM_FRAMES[Const.RUN_ANIM[characterID]]);
        }
    }

    /**
     * Returns the gun object the player is currently using.
     * @return the PlayerGun object.
     */
    public PlayerGun getGun() {
        return gun;
    }

    /**
     * Returns the player's stats.
     * @return the PlayerStats object.
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     * Returns the player's controller.
     * @return the PlayerController object.
     */
    public PlayerController getController() {
        return pc;
    }

    /**
     * Returns the player's input reader.
     * @return the PlayerInputReader object.
     */
    public PlayerInputReader getInputReader() {
        return pi;
    }

    /**
     * Gets the camera's current position including any shake offset.
     * @param dimensionID specified dimension ID as stated in Const.
     * @return the current camera position in that dimension.
     */
    public double getCameraPerspective(int dimensionID) {
        return cam[dimensionID] + camShake[dimensionID];
    }

    /**
     * Gets the target position the camera is trying to follow.
     * @param dimensionID specified dimension ID as stated in Const.
     * @return the camera's target position in that dimension.
     */
    public double getTargetPerspective(int dimensionID) {
        return targetCam[dimensionID];
    }

    @Override
    public boolean isDead() {
        return dead;
    }
    @Override
    public void moveX(double speed) {
        hitbox.setX(hitbox.getX() + speed);
        spriteBounds.setX(spriteBounds.getX() + speed);
    }
    @Override
    public void moveY(double speed) {
        hitbox.setY(hitbox.getY() + speed);
        spriteBounds.setY(spriteBounds.getY() + speed);
    }
    @Override
    public void takeDamage(double d) {
        hp -= d;
        dead = hp <= 0;
        if (hp <= 0) hp = 0;
    }
    @Override
    public void setHealth(double health) {
        hp = health;
        dead = hp <= 0;
    }
    @Override
    public void takeKnockback(double k, double trajectory) {
        knocked = true;
        knockback[Const.X] = k*Math.cos(trajectory);
        knockback[Const.Y] = k*Math.sin(trajectory);
    }
    @Override 
    public void endKnockback() {
        knocked = false;
    }
    @Override
    public boolean isKnocked() {
        return knocked;
    }
    @Override
    public double getHP() {
        return hp;
    }
    @Override
    public double getMaxHP() {
        return Const.CHARACTER_HP;
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
    public double getHitbox(int dimensionID) {
        switch (dimensionID) {
            case Const.X: return hitbox.getX();
            case Const.Y: return hitbox.getY();
            case Const.WIDTH: return hitbox.getWidth();
            case Const.HEIGHT: return hitbox.getHeight();
            default: return 0;
        }
    }
    @Override
    public double getSpeed(int dimensionID) {
        return speed[dimensionID];
    }
    @Override
    public int getLayer() {
        return (int) (getHitbox(Const.Y));
    }
}
