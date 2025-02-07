package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModServerConfigs {
	
	public static ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
	static {
		SERVER_BUILDER.comment("Durations of the cake potion effects").push("Potion durations");
	}
	public static final ForgeConfigSpec.IntValue POISON_CAKE_POISON_DUR = registerPotionDur("poisonCakePoisonDur", 300);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_REGEN_DUR = registerPotionDur("goldenAppleCakeRegenDur", 200);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_RES_DUR = registerPotionDur("goldenAppleCakeResDur", 3000);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_ABSORPTION_DUR = registerPotionDur("goldenAppleCakeAbsorptionDur", 1200);
	public static final ForgeConfigSpec.IntValue FIREY_CAKE_FIRE_RES_DUR = registerPotionDur("fireyCakeFireResDur", 300);
	public static final ForgeConfigSpec.IntValue FIREY_CAKE_ON_FIRE_DUR = registerPotionDur("fireyCakeOnFireDur", 20);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_JUMP_BOOST_DUR = registerPotionDur("slimeCakeJumpBoostDur", 1200);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_RES_DUR = registerPotionDur("slimeCakeResDur", 1200);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_BOUNCING_DUR = registerPotionDur("slimeCakeBouncingDur", 1200);
	public static final ForgeConfigSpec.IntValue BEETROOT_CAKE_REGEN_DUR = registerPotionDur("beetrootCakeRegenDur", 100);
	public static final ForgeConfigSpec.IntValue LAVA_CAKE_REGEN_DUR = registerPotionDur("lavaCakeRegenDur", 300);
	public static final ForgeConfigSpec.IntValue LAVA_CAKE_STRENGTH_DUR = registerPotionDur("lavaCakeStrengthDur", 300);
	public static final ForgeConfigSpec.IntValue ICE_CAKE_NIGHT_VISION_DUR = registerPotionDur("iceCakeNightVisionDur", 2400);
	public static final ForgeConfigSpec.IntValue CHORUS_CAKE_LEVITATION_DUR = registerPotionDur("chorusCakeLevitationDur", 300);
	public static final ForgeConfigSpec.IntValue GLOWSTONE_CAKE_GLOWING_DUR = registerPotionDur("glowstoneCakeGlowingDur", 85);
	static {
		SERVER_BUILDER.pop();
		SERVER_BUILDER.comment("Strengths of the cake potion effects").push("Potion strengths");
	}
	public static final ForgeConfigSpec.IntValue POISON_CAKE_POISON_STRENGTH = registerPotionStrength("poisonCakePoisonStrength", 1);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_REGEN_STRENGTH = registerPotionStrength("goldenAppleCakeRegenStrength", 1);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_RES_STRENGTH = registerPotionStrength("goldenAppleCakeResStrength", 0);
	public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_ABSORPTION_STRENGTH = registerPotionStrength("goldenAppleCakeAbsorptionStrength", 1);
	public static final ForgeConfigSpec.IntValue FIREY_CAKE_FIRE_RES_STRENGTH = registerPotionStrength("fireyCakeFireResStrength", 1);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_JUMP_BOOST_STRENGTH = registerPotionStrength("slimeCakeJumpBoostStrength", 0);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_RES_STRENGTH = registerPotionStrength("slimeCakeResStrength", 0);
	public static final ForgeConfigSpec.IntValue SLIME_CAKE_BOUNCING_STRENGTH = registerPotionStrength("slimeCakeBouncingStrength", 0);
	public static final ForgeConfigSpec.IntValue BEETROOT_CAKE_REGEN_STRENGTH = registerPotionStrength("beetrootCakeRegenStrength", 1);
	public static final ForgeConfigSpec.IntValue LAVA_CAKE_REGEN_STRENGTH = registerPotionStrength("lavaCakeRegenStrength", 1);
	public static final ForgeConfigSpec.IntValue LAVA_CAKE_STRENGTH_STRENGTH = registerPotionStrength("lavaCakeStrengthStrength", 1);
	public static final ForgeConfigSpec.IntValue ICE_CAKE_NIGHT_VISION_STRENGTH = registerPotionStrength("iceCakeNightVisionStrength", 0);
	public static final ForgeConfigSpec.IntValue CHORUS_CAKE_LEVITATION_STRENGTH = registerPotionStrength("chorusCakeLevitationStrength", 1);
	static {
		SERVER_BUILDER.pop();
		SERVER_BUILDER.comment("Miscellaneous configs").push("Misc");
	}
	public static final ForgeConfigSpec.DoubleValue CHORUS_CAKE_TELEPORT_RADIUS = SERVER_BUILDER.comment("The radius in which the player will be randomly teleported").defineInRange("chorusCakeTeleportRadius", 10, 0, 10000D);
	public static final ForgeConfigSpec.DoubleValue ENDER_CAKE_TELEPORT_RADIUS = SERVER_BUILDER.comment("The radius in which the player will be randomly teleported").defineInRange("enderCakeTeleportRadius", 50, 0, 10000D);
	public static final ForgeConfigSpec.BooleanValue EFFECTED_BY_REDSTONE = SERVER_BUILDER.comment("Whether the TNT cake will explode when powered by Redstone").define("effectedByRedstone", false);
	
	public static final ForgeConfigSpec SERVERSPEC = SERVER_BUILDER.build();

	private static ForgeConfigSpec.IntValue registerPotionDur(final String field, final int defaultInt) {
		return SERVER_BUILDER.defineInRange(field, defaultInt, 0, 32000);
	}

	private static ForgeConfigSpec.IntValue registerPotionStrength(final String field, final int defaultInt) {
		return SERVER_BUILDER.defineInRange(field, defaultInt, 0, 9);
	}
}