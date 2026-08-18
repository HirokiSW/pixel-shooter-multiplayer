/**
    This class holds constant values used across the game. 
    Includes IDs and values for directions, character and gun identifiers, sprite IDs, 
    game configuration values, and map coordinates.

    This class is final and cannot be modified.
    All values are public static final for all files to access.

    Contains the follwing constants grouped by category:
    Direction IDs,
    Dimension IDs,
    Data Type IDs,
    Gun Stats and Identifiers,
    Character Data,
    Map and Teleportation Details,
    Sprite Assets

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

public final class Const {

    // Direction IDs
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3; 
    public static final int SWITCH = 4;
    public static final int DASH = 5;
    public static final int RELOAD = 6;
    
    // Dimension IDs
    public static final int X = 0;
    public static final int Y = 1;
    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;
    public static final int ROTATION = 4;
    public static final int PIVOT_X = 5;
    public static final int PIVOT_Y = 6;

    // Data Type IDs
    public static final int TYPE_BUTTON_PRESSED = 0;
    public static final int TYPE_MOUSE_COORDS = 1;
    public static final int TYPE_MOUSE_PRESSED = 2;
    public static final int TYPE_PLAYER_COORDS = 3;
    public static final int TYPE_PLAYER_HEALTH = 4;

    // Gun Slot IDs
    public static final int PRIMARY = 0;
    public static final int SECONDARY = 1;

    // General
    public static final int MILISECONDS_PER_FRAME = 20;
    public static final int FRAME_WIDTH = 1024;
    public static final int FRAME_HEIGHT = 768;
    public static final double[] RANDOM_SEED = new double[100];

    /**
     * Sets the RANDOM_SEED array with the given the set of double value array argument
     * @param randomVariables An array containing 100 double values to initialize the RANDOM_SEED.
     */
    public static void setRandomVariables(double[] randomVariables){
        for (int i = 0; i < 100; i++) {
            RANDOM_SEED[i] = randomVariables[i];
        }
    }

    // Characters
    public static final int KNIGHT_ID = 0;
    public static final int LIZARD_ID = 1;
    public static final int DWARF_ID = 2;
    public static final int WIZZARD_ID = 3;
    public static final int ELF_ID = 4;
    public static final double CHARACTER_HP = 100;
    public static final double CHARACTER_WIDTH = 46;
    public static final double CHARACTER_HEIGHT = 70;
    public static final double RESPAWN_TIME = 4000;
    public static final double DASH_DURATION = 140;
    public static final double DASH_COOLDOWN = 1000;

    // Guns
    public static final int REVOLVER_ID = 0;
    public static final int PISTOL_ID = 1;
    public static final int UZI_ID = 2;
    public static final int SHORTY_ID = 3;
    public static final int SPREADER_ID = 4;
    public static final int ASSAULT_ID = 5;
    public static final int MACHINE_ID = 6;
    public static final int SNIPER_ID = 7;
    public static final int MARKSMAN_ID = 8;
    public static final int SHOTGUN_ID = 9;          
    public static final int HANDLE_WIDTH = 0;
    public static final int HANDLE_HEIGHT = 1;
    public static final int BARREL_LENGTH = 2;
    public static final int BARREL_HEIGHT = 3;
    public static final int BULLET_WIDTH = 4;
    public static final int BULLET_HEIGHT = 5;
    public static final int BULLET_CAPACITY = 6;
    public static final int BULLET_COUNT = 7;
    public static final int MOVE_SPEED = 8;
    public static final int SWITCH_TIME = 9;
    public static final int RECOIL = 10;
    public static final int KNOCKBACK = 11;
    public static final int SPREAD = 12;
    public static final int SHOOT_INTERVAL = 13;
    public static final int RELOAD_TIME = 14;
    public static final int DAMAGE = 15;
    public static final int BULLET_SPEED = 16;
    public static final int VISION_RANGE = 17;
    public static final int BULLETS_LEFT = 18;
    public static final double[][] GUN_STATS = new double[10][18];
    static {
        GUN_STATS[ASSAULT_ID]   = new double[] {48, 21, 15, 9 , 30, 12, 30, 1, 8 , 600, 10, 10, 12, 100, 1500, 9, 30, 0.15};
        GUN_STATS[MARKSMAN_ID]  = new double[] {54, 21, 27, 6 , 30, 18, 8 , 1, 8 , 250, 25, 16, 4 , 450, 1800, 30, 40, 0.25}; 
        GUN_STATS[MACHINE_ID]   = new double[] {57, 18, 24, 9 , 30, 12, 50, 1, 5 , 200, 10, 8 , 25, 50 , 2000, 7, 30, 0.2}; 
        GUN_STATS[SHOTGUN_ID]   = new double[] {39, 18, 27, 3 , 40, 13, 5 , 7, 9 , 300, 30, 20, 45, 600, 2000, 10, 30, 0.1};
        GUN_STATS[SNIPER_ID]    = new double[] {60, 18, 36, 21, 30, 18, 7 , 1, 7 , 500, 40, 30, 0 , 1000,2500, 50, 50, 0.4};
        GUN_STATS[SPREADER_ID]  = new double[] {42, 15, 15, 9 , 30, 18, 8 , 3, 10, 200, 15, 6 , 20, 250, 750 , 8, 25, 0.08};
        GUN_STATS[PISTOL_ID]    = new double[] {21, 18, 9 , 6 , 30, 18, 12, 1, 11, 200, 5 , 4 , 8 , 120, 400 , 8, 25, 0.08};
        GUN_STATS[REVOLVER_ID]  = new double[] {27, 18, 9 , 9 , 30, 18, 6 , 1, 11, 200, 8 , 10, 5 , 300, 700 , 20, 30, 0.12};
        GUN_STATS[SHORTY_ID]    = new double[] {42, 15, 15, 6 , 40, 13, 2 , 5, 12, 250, 15, 12, 60, 200, 800 , 10, 30, 0};
        GUN_STATS[UZI_ID]       = new double[] {36, 24, 6 , 9 , 15, 9 , 24, 1, 10, 500, 4 , 4 , 30, 45 , 600 , 5, 25, 0.1};
    }

    // Maps
    public static final int NUMBER_OF_TELEPORTERS = 4;
    public static final int LOBBY_ID = 0;
    public static final int DUNGEON_ID = 1;
    public static final int PVP_ID = 2;
    public static final double[][] MAP_START = {
        {0, 0},
        {5000, 0},
        {-5000, 0}
    };
    public static final double[][] TELEPORT_SPAWNPOINT = {
        {MAP_START[LOBBY_ID][X] + 3545, MAP_START[LOBBY_ID][Y] + 1859},
        {MAP_START[DUNGEON_ID][X] + 650, MAP_START[DUNGEON_ID][Y] + 420},
        {MAP_START[PVP_ID][X] + 650, MAP_START[PVP_ID][Y] + 420}
    };
    public static final int[] TELEPORT_DESTINATION = {DUNGEON_ID, PVP_ID, LOBBY_ID, LOBBY_ID};
    public static final double[] TELEPORT_DURATION = {3000, 3000, 5000, 5000};

    // Sprites
    public static final int NUMBER_OF_SPRITES = 34;
    public static final int LOBBY_BACK_BG = 10;
    public static final int DUNGEON_BG = 11;
    public static final int PVP_BACK_BG = 12;
    public static final int EMPTY_HEALTH = 13;
    public static final int EMPTY_AMMO_PRIMARY = 14;
    public static final int EMPTY_AMMO_SECONDARY = 15;
    public static final int GREEN_BAR = 16;
    public static final int YELLOW_BAR = 17;
    public static final int MARKINGS = 18;
    public static final int BENCH = 19;
    public static final int TREE_SML = 20;
    public static final int TREE_MED = 21;
    public static final int TREE_LRG = 22;
    public static final int COLUMN_SML = 23;
    public static final int COLUMN_LRG = 24;
    public static final int STATUE = 25;
    public static final int GRAVE_MINI = 26;
    public static final int GRAVE_CROSS = 27;
    public static final int GRAVE_RIP = 28;
    public static final int SCRIBE_SML = 29;
    public static final int SCRIBE_MED = 30;
    public static final int SCRIBE_LRG = 31;
    public static final int BOX = 32;
    public static final int LOBBY_SHADOWS = 33;
    public static final String[] SPRITE_NAMES = new String[NUMBER_OF_SPRITES];
    static {
        SPRITE_NAMES[REVOLVER_ID] = "revolver0";
        SPRITE_NAMES[PISTOL_ID] = "pistol0";
        SPRITE_NAMES[UZI_ID] = "uzi0";
        SPRITE_NAMES[SHORTY_ID] = "shorty0";
        SPRITE_NAMES[SPREADER_ID] = "burst0";
        SPRITE_NAMES[ASSAULT_ID] = "assault0";
        SPRITE_NAMES[MACHINE_ID] = "machine0";
        SPRITE_NAMES[SNIPER_ID] = "sniper0";
        SPRITE_NAMES[MARKSMAN_ID] = "marksman0";
        SPRITE_NAMES[SHOTGUN_ID] = "shotgun0";
        SPRITE_NAMES[LOBBY_BACK_BG] = "background";
        SPRITE_NAMES[DUNGEON_BG] = "dungeon";
        SPRITE_NAMES[PVP_BACK_BG] = "pvp_back";
        SPRITE_NAMES[EMPTY_HEALTH] = "empty_health_bar";
        SPRITE_NAMES[EMPTY_AMMO_PRIMARY] = "empty_primary_ammo_bar";
        SPRITE_NAMES[EMPTY_AMMO_SECONDARY] = "empty_secondary_ammo_bar";
        SPRITE_NAMES[GREEN_BAR] = "health_bar";
        SPRITE_NAMES[YELLOW_BAR] = "ammo_bar";
        SPRITE_NAMES[MARKINGS] = "markings";
        SPRITE_NAMES[BENCH] = "bench";
        SPRITE_NAMES[TREE_SML] = "tree_sml";
        SPRITE_NAMES[TREE_MED] = "tree_med";
        SPRITE_NAMES[TREE_LRG] = "tree_lrg";
        SPRITE_NAMES[COLUMN_SML] = "column_sml";
        SPRITE_NAMES[COLUMN_LRG] = "column_lrg";
        SPRITE_NAMES[STATUE] = "statue";
        SPRITE_NAMES[GRAVE_MINI] = "grave_mini";
        SPRITE_NAMES[GRAVE_CROSS] = "grave_cross";
        SPRITE_NAMES[GRAVE_RIP] = "grave_rip";
        SPRITE_NAMES[SCRIBE_SML] = "scribe_sml";
        SPRITE_NAMES[SCRIBE_MED] = "scribe_med";
        SPRITE_NAMES[SCRIBE_LRG] = "scribe_lrg";
        SPRITE_NAMES[BOX] = "box";
        SPRITE_NAMES[LOBBY_SHADOWS] = "shadows";
    }

    // Animation Constants
    public static final int NUMBER_OF_ANIMATIONS = 29;
    public static final int[] IDLE_ANIM = new int[5];
    public static final int[] HIT_ANIM = new int[5];
    public static final int[] RUN_ANIM = new int[5];
    public static final int[] AMMO_ANIM = new int[10];
    static {
        IDLE_ANIM[KNIGHT_ID] = 0;
        IDLE_ANIM[LIZARD_ID] = 1;
        IDLE_ANIM[DWARF_ID] = 2;
        IDLE_ANIM[WIZZARD_ID] = 3;
        IDLE_ANIM[ELF_ID] = 4;
        HIT_ANIM[KNIGHT_ID] = 5;
        HIT_ANIM[LIZARD_ID] = 6;
        HIT_ANIM[DWARF_ID] = 7;
        HIT_ANIM[WIZZARD_ID] = 8;
        HIT_ANIM[ELF_ID] = 9;
        RUN_ANIM[KNIGHT_ID] = 10;
        RUN_ANIM[LIZARD_ID] = 11;
        RUN_ANIM[DWARF_ID] = 12;
        RUN_ANIM[WIZZARD_ID] = 13;
        RUN_ANIM[ELF_ID] = 14;
        AMMO_ANIM[REVOLVER_ID] = 15;
        AMMO_ANIM[PISTOL_ID] = 16;
        AMMO_ANIM[UZI_ID] = 17;
        AMMO_ANIM[SHORTY_ID] = 18;
        AMMO_ANIM[SPREADER_ID] = 19;
        AMMO_ANIM[ASSAULT_ID] = 20;
        AMMO_ANIM[MACHINE_ID] = 21;
        AMMO_ANIM[SNIPER_ID] = 22;
        AMMO_ANIM[MARKSMAN_ID] = 23;
        AMMO_ANIM[SHOTGUN_ID] = 24;
    }
    public static final int SHOT = 25;
    public static final int IMPACT = 26;
    public static final int RED_FOUNTAIN = 27;
    public static final int BLUE_FOUNTAIN = 28;
    public static final int[] ANIM_FRAMES = new int[NUMBER_OF_ANIMATIONS];
    public static final double[] ANIM_SPEED = new double[NUMBER_OF_ANIMATIONS];
    static {
        for (int i = 0; i < NUMBER_OF_ANIMATIONS; i++) {
            if (i < 5) {
                ANIM_SPEED[i] = 0.2;
                ANIM_FRAMES[i] = 4;
            } else if (i < 10) {
                ANIM_SPEED[i] = 0.25;
                ANIM_FRAMES[i] = 5;
            } else if (i < 15) {
                ANIM_SPEED[i] = 0.4;
                ANIM_FRAMES[i] = 4;
            } else if (i < 25) {
                ANIM_SPEED[i] = 1;
                ANIM_FRAMES[i] = 4;
            } else if (i < 27) {
                ANIM_SPEED[i] = 1;
                ANIM_FRAMES[i] = 2;
            } else {
                ANIM_SPEED[i] = 0.2;
                ANIM_FRAMES[i] = 3;
            }
        }
    }
    public static final String[] ANIM_NAMES = new String[NUMBER_OF_ANIMATIONS];
    static {
        ANIM_NAMES[IDLE_ANIM[KNIGHT_ID]] = "knight_idle";
        ANIM_NAMES[IDLE_ANIM[LIZARD_ID]] = "lizard_idle";
        ANIM_NAMES[IDLE_ANIM[DWARF_ID]] = "dwarf_idle";
        ANIM_NAMES[IDLE_ANIM[WIZZARD_ID]] = "wizzard_idle";
        ANIM_NAMES[IDLE_ANIM[ELF_ID]] = "elf_idle";
        ANIM_NAMES[HIT_ANIM[KNIGHT_ID]] = "knight_hit";
        ANIM_NAMES[HIT_ANIM[LIZARD_ID]] = "lizard_hit";
        ANIM_NAMES[HIT_ANIM[DWARF_ID]] = "dwarf_hit";
        ANIM_NAMES[HIT_ANIM[WIZZARD_ID]] = "wizzard_hit";
        ANIM_NAMES[HIT_ANIM[ELF_ID]] = "elf_hit";
        ANIM_NAMES[RUN_ANIM[KNIGHT_ID]] = "knight_run";
        ANIM_NAMES[RUN_ANIM[LIZARD_ID]] = "lizard_run";
        ANIM_NAMES[RUN_ANIM[DWARF_ID]] = "dwarf_run";
        ANIM_NAMES[RUN_ANIM[WIZZARD_ID]] = "wizzard_run";
        ANIM_NAMES[RUN_ANIM[ELF_ID]] = "elf_run";
        ANIM_NAMES[AMMO_ANIM[REVOLVER_ID]] = "revolver_ammo";
        ANIM_NAMES[AMMO_ANIM[PISTOL_ID]] = "revolver_ammo";
        ANIM_NAMES[AMMO_ANIM[UZI_ID]] = "smg_ammo";
        ANIM_NAMES[AMMO_ANIM[SHORTY_ID]] = "shotgun_ammo";
        ANIM_NAMES[AMMO_ANIM[SPREADER_ID]] = "revolver_ammo";
        ANIM_NAMES[AMMO_ANIM[ASSAULT_ID]] = "rifle_ammo";
        ANIM_NAMES[AMMO_ANIM[MACHINE_ID]] = "rifle_ammo";
        ANIM_NAMES[AMMO_ANIM[SNIPER_ID]] = "sniper_ammo";
        ANIM_NAMES[AMMO_ANIM[MARKSMAN_ID]] = "sniper_ammo";
        ANIM_NAMES[AMMO_ANIM[SHOTGUN_ID]] = "shotgun_ammo";
        ANIM_NAMES[SHOT] = "shoot";
        ANIM_NAMES[IMPACT] = "impact";
        ANIM_NAMES[RED_FOUNTAIN] = "red_fountain";
        ANIM_NAMES[BLUE_FOUNTAIN] = "blue_fountain";
    }
}