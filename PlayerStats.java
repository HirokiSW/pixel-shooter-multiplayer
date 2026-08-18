/**
    This class is responsible for displaying the health status of the 
    Player and the bullet status of both guns that Player holds.
    The gun currently not in use, its respective bar is overlayed 
    by a translucent black bar to make it easy to indicate.

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

public class PlayerStats implements SpriteDrawing {
    
    private Player body;

    /**
     * Constructs the PlayerStats for the given Player.
     * 
     * @param body the Player whose status will be displayed.
     */
    public PlayerStats(Player body) {
        this.body = body;
    }

    @Override
    public void draw(Graphics2D g2d, SpriteManager sm) {
        sm.drawSprite(g2d, Const.EMPTY_HEALTH, 50, 30, 24, 24);
        sm.drawSprite(g2d, Const.GREEN_BAR, 74, 30, Math.max(0, (int) (200*(body.getHP()/body.getMaxHP()))), 24);
        for (int i = 0; i < 2; i++) {
            int y = 60 + i * 30;
            double bulletRatio = body.getGun().getGunStat(i, Const.BULLETS_LEFT) / body.getGun().getGunStat(i, Const.BULLET_CAPACITY);
            int barWidth = (int)(200 * bulletRatio);
            sm.drawSprite(g2d, Const.EMPTY_AMMO_PRIMARY + i, 50, y, 24, 24);
            sm.drawSprite(g2d, Const.YELLOW_BAR, 74, y, barWidth, 24);
            if (!(body.getGun().getCurrGunNum() == i)) {
                g2d.setColor(new Color(0, 0, 0, 128));
                g2d.fillRect(50, y, 24 + barWidth, 24);
            }
        }
    }
}

