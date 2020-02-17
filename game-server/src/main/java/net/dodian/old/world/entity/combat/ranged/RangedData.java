package net.dodian.old.world.entity.combat.ranged;

import java.util.HashMap;
import java.util.Map;

import net.dodian.old.engine.task.impl.CombatPoisonEffect.PoisonType;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.container.impl.Equipment;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A table of constants that hold data for all ranged ammo.
 * 
 * Edit: This is now purely only data.
 * Updated it and moved all methods to CombatFactory.
 * 
 * 
 * @author Swiffy96
 * @author Professor Oak
 */
public class RangedData {

	/** A map of items and their respective interfaces. */
	private static Map<Integer, RangedWeaponData> range_wep_data = new HashMap<>();
	private static Map<Integer, AmmunitionData> range_ammo_data = new HashMap<>();

	public enum RangedWeaponData {

		LONGBOW(new int[] {839}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW}, RangedWeaponType.LONGBOW),
		SHORTBOW(new int[] {841}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW}, RangedWeaponType.SHORTBOW),
		OAK_LONGBOW(new int[] {845}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW}, RangedWeaponType.LONGBOW),
		OAK_SHORTBOW(new int[] {843}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW}, RangedWeaponType.SHORTBOW),
		WILLOW_LONGBOW(new int[] {847}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW}, RangedWeaponType.LONGBOW),
		WILLOW_SHORTBOW(new int[] {849}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW}, RangedWeaponType.SHORTBOW),
		MAPLE_LONGBOW(new int[] {851}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW}, RangedWeaponType.LONGBOW),
		MAPLE_SHORTBOW(new int[] {853}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW}, RangedWeaponType.SHORTBOW),
		YEW_LONGBOW(new int[] {855}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW}, RangedWeaponType.LONGBOW),
		YEW_SHORTBOW(new int[] {857}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW}, RangedWeaponType.SHORTBOW),
		MAGIC_LONGBOW(new int[] {859}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.LONGBOW),
		MAGIC_SHORTBOW(new int[] {861, 6724}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.SHORTBOW),
		GODBOW(new int[] {19143, 19149, 19146}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.SHORTBOW),
		ZARYTE_BOW(new int[] {20171}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.SHORTBOW),

		DARK_BOW(new int[] {11235, 13405, 15701, 15702, 15703, 15704}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.DARK_BOW),

		BRONZE_CROSSBOW(new int[] {9174}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT}, RangedWeaponType.CROSSBOW),
		IRON_CROSSBOW(new int[] {9177}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT}, RangedWeaponType.CROSSBOW),
		STEEL_CROSSBOW(new int[] {9179}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.ENCHANTED_JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.ENCHANTED_PEARL_BOLT}, RangedWeaponType.CROSSBOW),
		MITHRIL_CROSSBOW(new int[] {9181}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.ENCHANTED_JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.ENCHANTED_PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ENCHANTED_TOPAZ_BOLT}, RangedWeaponType.CROSSBOW),
		ADAMANT_CROSSBOW(new int[] {9183}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.ENCHANTED_JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.ENCHANTED_PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ENCHANTED_TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.ENCHANTED_SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ENCHANTED_EMERALD_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.ENCHANTED_RUBY_BOLT}, RangedWeaponType.CROSSBOW),
		RUNE_CROSSBOW(new int[] {9185}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.ENCHANTED_JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.ENCHANTED_PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ENCHANTED_TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.ENCHANTED_SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ENCHANTED_EMERALD_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.ENCHANTED_RUBY_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.ENCHANTED_DIAMOND_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.ENCHANTED_ONYX_BOLT, AmmunitionData.DRAGON_BOLT, AmmunitionData.ENCHANTED_DRAGON_BOLT}, RangedWeaponType.CROSSBOW),
		ARMADYL_CROSSBOW(new int[] {11785}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.ENCHANTED_OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.ENCHANTED_JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.ENCHANTED_PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ENCHANTED_TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.ENCHANTED_SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ENCHANTED_EMERALD_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.ENCHANTED_RUBY_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.ENCHANTED_DIAMOND_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.ENCHANTED_ONYX_BOLT, AmmunitionData.DRAGON_BOLT, AmmunitionData.ENCHANTED_DRAGON_BOLT}, RangedWeaponType.CROSSBOW),

		BRONZE_DART(new int[] {806}, new AmmunitionData[] {AmmunitionData.BRONZE_DART}, RangedWeaponType.THROW),
		IRON_DART(new int[] {807}, new AmmunitionData[] {AmmunitionData.IRON_DART}, RangedWeaponType.THROW),
		STEEL_DART(new int[] {808}, new AmmunitionData[] {AmmunitionData.STEEL_DART}, RangedWeaponType.THROW),
		MITHRIL_DART(new int[] {809}, new AmmunitionData[] {AmmunitionData.MITHRIL_DART}, RangedWeaponType.THROW),
		ADAMANT_DART(new int[] {810}, new AmmunitionData[] {AmmunitionData.ADAMANT_DART}, RangedWeaponType.THROW),
		RUNE_DART(new int[] {811}, new AmmunitionData[] {AmmunitionData.RUNE_DART}, RangedWeaponType.THROW),
		DRAGON_DART(new int[] {11230}, new AmmunitionData[] {AmmunitionData.DRAGON_DART}, RangedWeaponType.THROW),

		BRONZE_KNIFE(new int[] {864, 870, 5654}, new AmmunitionData[] {AmmunitionData.BRONZE_KNIFE}, RangedWeaponType.THROW),
		IRON_KNIFE(new int[] {863, 871, 5655}, new AmmunitionData[] {AmmunitionData.IRON_KNIFE}, RangedWeaponType.THROW),
		STEEL_KNIFE(new int[] {865, 872, 5656}, new AmmunitionData[] {AmmunitionData.STEEL_KNIFE}, RangedWeaponType.THROW),
		BLACK_KNIFE(new int[] {869, 874, 5658}, new AmmunitionData[] {AmmunitionData.BLACK_KNIFE}, RangedWeaponType.THROW),
		MITHRIL_KNIFE(new int[] {866, 873, 5657}, new AmmunitionData[] {AmmunitionData.MITHRIL_KNIFE}, RangedWeaponType.THROW),
		ADAMANT_KNIFE(new int[] {867, 875, 5659}, new AmmunitionData[] {AmmunitionData.ADAMANT_KNIFE}, RangedWeaponType.THROW),
		RUNE_KNIFE(new int[] {868, 876, 5660, 5667}, new AmmunitionData[] {AmmunitionData.RUNE_KNIFE}, RangedWeaponType.THROW),


		TOKTZ_XIL_UL(new int[] {6522}, new AmmunitionData[] {AmmunitionData.TOKTZ_XIL_UL}, RangedWeaponType.THROW),

		KARILS_CROSSBOW(new int[]{4734}, new AmmunitionData[] {AmmunitionData.BOLT_RACK}, RangedWeaponType.CROSSBOW),

		BALLISTA(new int[]{19478, 19481}, new AmmunitionData[] {AmmunitionData.BRONZE_JAVELIN, AmmunitionData.IRON_JAVELIN, AmmunitionData.STEEL_JAVELIN, AmmunitionData.MITHRIL_JAVELIN, AmmunitionData.ADAMANT_JAVELIN, AmmunitionData.RUNE_JAVELIN, AmmunitionData.DRAGON_JAVELIN}, RangedWeaponType.BALLISTA),
		
		TOXIC_BLOWPIPE(new int[]{12926}, new AmmunitionData[] {AmmunitionData.DRAGON_DART}, RangedWeaponType.BLOWPIPE);

		RangedWeaponData(int[] weaponIds, AmmunitionData[] ammunitionData, RangedWeaponType type) {
			this.weaponIds = weaponIds;
			this.ammunitionData = ammunitionData;
			this.type = type;
		}

		private int[] weaponIds;
		private AmmunitionData[] ammunitionData;
		private RangedWeaponType type;

		public int[] getWeaponIds() {
			return weaponIds;
		}

		public AmmunitionData[] getAmmunitionData() {
			return ammunitionData;
		}

		public RangedWeaponType getType() {
			return type;
		}

		public static RangedWeaponData getFor(Player p) {
			int weapon = p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
			return range_wep_data.get(weapon);
		}

		static {
			for(RangedWeaponData data : RangedWeaponData.values()) {
				for(int i : data.getWeaponIds()) {
					range_wep_data.put(i, data);
				}
			}
		}
	}

	public enum AmmunitionData {

		BRONZE_ARROW(882, new Graphic(19, GraphicHeight.HIGH), 10, 7),
		IRON_ARROW(884, new Graphic(18, GraphicHeight.HIGH), 9, 10),
		STEEL_ARROW(886, new Graphic(20, GraphicHeight.HIGH), 11, 16),
		MITHRIL_ARROW(888, new Graphic(21, GraphicHeight.HIGH), 12, 22),
		ADAMANT_ARROW(890, new Graphic(22, GraphicHeight.HIGH), 13, 31),
		RUNE_ARROW(892, new Graphic(24, GraphicHeight.HIGH), 15, 50),
		ICE_ARROW(78, new Graphic(25, GraphicHeight.HIGH), 16, 58),
		BROAD_ARROW(4160, new Graphic(20,GraphicHeight.HIGH), 11, 58),
		DRAGON_ARROW(11212, new Graphic(1111, GraphicHeight.HIGH), 1120, 65),

		BRONZE_BOLT(877, new Graphic(955, GraphicHeight.HIGH), 27,13),
		OPAL_BOLT(879, new Graphic(955, GraphicHeight.HIGH), 27,20),
		ENCHANTED_OPAL_BOLT(9236, new Graphic(955, GraphicHeight.HIGH), 27,20),
		IRON_BOLT(9140, new Graphic(955, GraphicHeight.HIGH), 27,28),
		JADE_BOLT(9335, new Graphic(955, GraphicHeight.HIGH), 27,31),
		ENCHANTED_JADE_BOLT(9237, new Graphic(955, GraphicHeight.HIGH), 27,31),
		STEEL_BOLT(9141, new Graphic(955, GraphicHeight.HIGH), 27,35),
		PEARL_BOLT(880, new Graphic(955, GraphicHeight.HIGH), 27,38),
		ENCHANTED_PEARL_BOLT(9238, new Graphic(955, GraphicHeight.HIGH), 27,38),
		MITHRIL_BOLT(9142, new Graphic(955, GraphicHeight.HIGH), 27,40),
		TOPAZ_BOLT(9336, new Graphic(955, GraphicHeight.HIGH), 27,50),
		ENCHANTED_TOPAZ_BOLT(9239, new Graphic(955, GraphicHeight.HIGH), 27,50),
		ADAMANT_BOLT(9143, new Graphic(955, GraphicHeight.HIGH), 27,60),
		SAPPHIRE_BOLT(9337, new Graphic(955, GraphicHeight.HIGH), 27,65),
		ENCHANTED_SAPPHIRE_BOLT(9240, new Graphic(955, GraphicHeight.HIGH), 27,65),
		EMERALD_BOLT(9338, new Graphic(955, GraphicHeight.HIGH), 27,70),
		ENCHANTED_EMERALD_BOLT(9241, new Graphic(955, GraphicHeight.HIGH), 27,70),
		RUBY_BOLT(9339, new Graphic(955, GraphicHeight.HIGH), 27,75),
		ENCHANTED_RUBY_BOLT(9242, new Graphic(955, GraphicHeight.HIGH), 27,75),
		BROAD_BOLT(13280, new Graphic(955, GraphicHeight.HIGH), 27,100),
		RUNITE_BOLT(9144, new Graphic(955, GraphicHeight.HIGH), 27,115),
		DIAMOND_BOLT(9340, new Graphic(955, GraphicHeight.HIGH), 27,105),
		ENCHANTED_DIAMOND_BOLT(9243, new Graphic(955, GraphicHeight.HIGH), 27,105),
		DRAGON_BOLT(9341, new Graphic(955, GraphicHeight.HIGH), 27,117),
		ENCHANTED_DRAGON_BOLT(9244, new Graphic(955, GraphicHeight.HIGH), 27,117),
		ONYX_BOLT(9342, new Graphic(955, GraphicHeight.HIGH), 27,120),
		ENCHANTED_ONYX_BOLT(9245, new Graphic(955, GraphicHeight.HIGH), 27,120),

		BRONZE_DART(806, new Graphic(232, GraphicHeight.HIGH), 226, 1),
		IRON_DART(807, new Graphic(233, GraphicHeight.HIGH), 227, 4),
		STEEL_DART(808, new Graphic(234, GraphicHeight.HIGH), 228, 6),
		MITHRIL_DART(809, new Graphic(235, GraphicHeight.HIGH), 229, 8),
		ADAMANT_DART(810, new Graphic(236, GraphicHeight.HIGH), 230, 13),
		RUNE_DART(811, new Graphic(237, GraphicHeight.HIGH), 231, 17),
		DRAGON_DART(11230, new Graphic(1123, GraphicHeight.HIGH), 226, 24),

		BRONZE_KNIFE(864, new Graphic(219, GraphicHeight.HIGH), 212, 3),
		BRONZE_KNIFE_P1(870, new Graphic(219, GraphicHeight.HIGH), 212, 3),
		BRONZE_KNIFE_P2(5654, new Graphic(219, GraphicHeight.HIGH), 212, 3),
		BRONZE_KNIFE_P3(5661, new Graphic(219, GraphicHeight.HIGH), 212, 3),

		IRON_KNIFE(863, new Graphic(220, GraphicHeight.HIGH), 213, 4),
		IRON_KNIFE_P1(871, new Graphic(220, GraphicHeight.HIGH), 213, 4),
		IRON_KNIFE_P2(5655, new Graphic(220, GraphicHeight.HIGH), 213, 4),
		IRON_KNIFE_P3(5662, new Graphic(220, GraphicHeight.HIGH), 213, 4),

		STEEL_KNIFE(865, new Graphic(221, GraphicHeight.HIGH), 214, 7),
		STEEL_KNIFE_P1(872, new Graphic(221, GraphicHeight.HIGH), 214, 7),
		STEEL_KNIFE_P2(5656, new Graphic(221, GraphicHeight.HIGH), 214, 7),
		STEEL_KNIFE_P3(5663, new Graphic(221, GraphicHeight.HIGH), 214, 7),

		BLACK_KNIFE(869, new Graphic(222, GraphicHeight.HIGH), 215, 8),
		BLACK_KNIFE_P1(874, new Graphic(222, GraphicHeight.HIGH), 215, 8),
		BLACK_KNIFE_P2(5658, new Graphic(222, GraphicHeight.HIGH), 215, 8),
		BLACK_KNIFE_P3(5665, new Graphic(222, GraphicHeight.HIGH), 215, 8),

		MITHRIL_KNIFE(866, new Graphic(223, GraphicHeight.HIGH), 215, 10),
		MITHRIL_KNIFE_P1(873, new Graphic(223, GraphicHeight.HIGH), 215, 10),
		MITHRIL_KNIFE_P2(5657, new Graphic(223, GraphicHeight.HIGH), 215, 10),
		MITHRIL_KNIFE_P3(5664, new Graphic(223, GraphicHeight.HIGH), 215, 10),

		ADAMANT_KNIFE(867, new Graphic(224, GraphicHeight.HIGH), 217, 14),
		ADAMANT_KNIFE_P1(875, new Graphic(224, GraphicHeight.HIGH), 217, 14),
		ADAMANT_KNIFE_P2(5659, new Graphic(224, GraphicHeight.HIGH), 217, 14),
		ADAMANT_KNIFE_P3(5666, new Graphic(224, GraphicHeight.HIGH), 217, 14),

		RUNE_KNIFE(868, new Graphic(225, GraphicHeight.HIGH), 218, 24),
		RUNE_KNIFE_P1(876, new Graphic(225, GraphicHeight.HIGH), 218, 24),
		RUNE_KNIFE_P2(5660, new Graphic(225, GraphicHeight.HIGH), 218, 24),
		RUNE_KNIFE_P3(5667, new Graphic(225, GraphicHeight.HIGH), 218, 24),

		/*	BRONZE_THROWNAXE(800, 43, 36, 3, 44, 7),
		IRON_THROWNAXE(801, 42, 35, 3, 44, 9),
		STEEL_THROWNAXE(802, 44, 37, 3, 44, 11),
		MITHRIL_THROWNAXE(803, 45, 38, 3, 44, 13),
		ADAMANT_THROWNAXE(804, 46, 39, 3, 44, 15),
		RUNE_THROWNAXE(805, 48, 41, 3, 44, 17),*/

		BRONZE_JAVELIN(825, null, 200, 25),
		IRON_JAVELIN(826, null, 201, 42),
		STEEL_JAVELIN(827, null, 202, 64),
		MITHRIL_JAVELIN(828, null, 203, 85),
		ADAMANT_JAVELIN(829, null, 204, 107),
		RUNE_JAVELIN(830, null, 205, 124),
		DRAGON_JAVELIN(19484, null, 1301, 150),

		TOKTZ_XIL_UL(6522, null, 442, 58),

		BOLT_RACK(4740, null, 27, 55),

		;

		AmmunitionData(int itemId, Graphic startGfx, int projectileId, int strength) {
			this.itemId = itemId;
			this.startGfx = startGfx;
			this.projectileId = projectileId;
			this.strength = strength;
		}

		//Ammo that shouldn't be dropped on the floor
		private static final ImmutableSet<AmmunitionData> NO_GROUND_DROP = Sets.immutableEnumSet(BRONZE_JAVELIN, IRON_JAVELIN, STEEL_JAVELIN, ADAMANT_JAVELIN, RUNE_JAVELIN, DRAGON_JAVELIN);

		private int itemId;
		private final Graphic startGfx;
		private int projectileId;
		private int strength;

		public int getItemId() {
			return itemId;
		}

		public Graphic getStartGraphic() {
			return startGfx;
		}

		public int getProjectileId() {
			return projectileId;
		}

		public int getStrength() {
			return strength;
		}

		public boolean dropOnFloor() {
			return !NO_GROUND_DROP.contains(this);
		}

		public static AmmunitionData getFor(Player p) {
			//First try to get a throw weapon as ammo
			int weapon = p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
			AmmunitionData throwWeapon = range_ammo_data.get(weapon);
			
			//Toxic blowpipe should always fire dragon darts.
			if(weapon == 12926) {
				return AmmunitionData.DRAGON_DART;
			}

			//Didnt find one. Try arrows
			if(throwWeapon == null) {
				return range_ammo_data.get(p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId());
			}

			return throwWeapon;
		}
		
		public static AmmunitionData getFor(int item) {
			//First try to get a throw weapon as ammo
			AmmunitionData throwWeapon = range_ammo_data.get(item);

			//Didnt find one. Try arrows
			if(throwWeapon == null) {
				return range_ammo_data.get(item);
			}

			return throwWeapon;
		}

		static {
			for(AmmunitionData data : AmmunitionData.values()) {
				range_ammo_data.put(data.getItemId(), data);
			}
		}
	}

	public enum RangedWeaponType {

		THROW(4),

		LONGBOW(5),
		SHORTBOW(5),
		CROSSBOW(5),
		DARK_BOW(5),
		BALLISTA(5),
		BLOWPIPE(5);

		RangedWeaponType(int distanceRequired) {
			this.distanceRequired = distanceRequired;
		}

		private int distanceRequired;

		public int getDistanceRequired() {
			return distanceRequired;
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static double getSpecialEffectsMultiplier(Player p, Character target, int damage) {

		double multiplier = 1.0;

		//Todo: ENCHANTED_RUBY_BOLT
		switch(p.getCombat().getAmmunition()) {

		case ENCHANTED_DIAMOND_BOLT:

			target.performGraphic(new Graphic(758, GraphicHeight.MIDDLE));
			multiplier = 1.15;

			break;

		case ENCHANTED_DRAGON_BOLT:

			boolean multiply = true;
			if(target.isPlayer()) {
				Player t = target.getAsPlayer();
				multiply = !(!t.getCombat().getFireImmunityTimer().finished()
						|| t.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 1540 
						|| t.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 11283);
			}

			if(multiply) {
				target.performGraphic(new Graphic(756));
				multiplier = 1.31;
			}

			break;
		case ENCHANTED_EMERALD_BOLT:

			target.performGraphic(new Graphic(752));
			CombatFactory.poisonEntity(target, PoisonType.MILD);

			break;
		case ENCHANTED_JADE_BOLT:

			target.performGraphic(new Graphic(755));
			multiplier = 1.05;

			break;
		case ENCHANTED_ONYX_BOLT:

			target.performGraphic(new Graphic(753));
			multiplier = 1.26;
			int heal = (int) (damage * 0.25) + 10;
			p.getSkillManager().setCurrentLevel(Skill.HITPOINTS, p.getSkillManager().getCurrentLevel(Skill.HITPOINTS)+heal);
			if(p.getSkillManager().getCurrentLevel(Skill.HITPOINTS) >= 1120) {
				p.getSkillManager().setCurrentLevel(Skill.HITPOINTS, 1120);
			}
			p.getSkillManager().updateSkill(Skill.HITPOINTS);
			if(damage < 250 && Misc.getRandom(3) <= 1) {
				damage += 150 + Misc.getRandom(80);
			}

			break;

		case ENCHANTED_PEARL_BOLT:


			target.performGraphic(new Graphic(750));
			multiplier = 1.1;


			break;

		case ENCHANTED_RUBY_BOLT:


			break;
		case ENCHANTED_SAPPHIRE_BOLT:

			target.performGraphic(new Graphic(751));
			if(target.isPlayer()) {
				Player t = target.getAsPlayer();
				t.getSkillManager().setCurrentLevel(Skill.PRAYER, t.getSkillManager().getCurrentLevel(Skill.PRAYER) - 20);
				if(t.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
					t.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
				}
				t.getPacketSender().sendMessage("Your Prayer level has been leeched.");

				p.getSkillManager().setCurrentLevel(Skill.PRAYER, t.getSkillManager().getCurrentLevel(Skill.PRAYER) + 20);
				if(p.getSkillManager().getCurrentLevel(Skill.PRAYER) > p.getSkillManager().getMaxLevel(Skill.PRAYER)) {
					p.getSkillManager().setCurrentLevel(Skill.PRAYER, p.getSkillManager().getMaxLevel(Skill.PRAYER));
				} else {
					p.getPacketSender().sendMessage("Your enchanced bolts leech some Prayer points from your opponent..");
				}
			}


			break;
		case ENCHANTED_TOPAZ_BOLT:

			target.performGraphic(new Graphic(757));
			if(target.isPlayer()) {
				Player t = target.getAsPlayer();
				t.getSkillManager().setCurrentLevel(Skill.MAGIC, t.getSkillManager().getCurrentLevel(Skill.MAGIC) - 3);
				t.getPacketSender().sendMessage("Your Magic level has been reduced.");
			}

			break;
		case ENCHANTED_OPAL_BOLT:

			target.performGraphic(new Graphic(749));
			multiplier = 1.3;			

			break;

		}

		return multiplier;
	}

}
