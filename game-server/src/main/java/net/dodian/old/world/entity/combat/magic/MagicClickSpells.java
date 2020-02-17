package net.dodian.old.world.entity.combat.magic;

import java.util.Optional;

import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.EffectTimer;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.MagicSpellbook;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.entity.impl.Character;

public class MagicClickSpells {

	private static enum MagicSpells {

		BONES_TO_BANANAS(new Spell() {

			@Override
			public int spellId() {
				return 1159;
			}

			@Override
			public int levelRequired() {
				return 15;
			}

			@Override
			public int baseExperience() {
				return 650;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(561), new Item(555, 2), new Item(557, 2)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		LOW_ALCHEMY(new Spell() {

			@Override
			public int spellId() {
				return 1162;
			}

			@Override
			public int levelRequired() {
				return 21;
			}

			@Override
			public int baseExperience() {
				return 4000;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(554, 3), new Item(561)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		TELEKINETIC_GRAB(new Spell() {

			@Override
			public int spellId() {
				return 1168;
			}

			@Override
			public int levelRequired() {
				return 33;
			}

			@Override
			public int baseExperience() {
				return 3988;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(563), new Item(556)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		SUPERHEAT_ITEM(new Spell() {

			@Override
			public int spellId() {
				return 1173;
			}

			@Override
			public int levelRequired() {
				return 43;
			}

			@Override
			public int baseExperience() {
				return 6544;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(554, 4), new Item(561)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		HIGH_ALCHEMY(new Spell() {

			@Override
			public int spellId() {
				return 1178;
			}

			@Override
			public int levelRequired() {
				return 55;
			}

			@Override
			public int baseExperience() {
				return 20000;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(554, 5), new Item(561)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		BONES_TO_PEACHES(new Spell() {

			@Override
			public int spellId() {
				return 15877;
			}

			@Override
			public int levelRequired() {
				return 60;
			}

			@Override
			public int baseExperience() {
				return 4121;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(561, 2), new Item(555, 4), new Item(557, 4)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}

		}),
		BAKE_PIE(new Spell() {

			@Override
			public int spellId() {
				return 30017;
			}

			@Override
			public int levelRequired() {
				return 65;
			}

			@Override
			public int baseExperience() {
				return 5121;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {

				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}
			
			@Override
			public MagicSpellbook getSpellbook() {
				return MagicSpellbook.LUNAR;
			}
		}),
		VENGEANCE_OTHER(new Spell() {

			@Override
			public int spellId() {
				return 30298;
			}

			@Override
			public int levelRequired() {
				return 93;
			}

			@Override
			public int baseExperience() {
				return 10000;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[] {new Item(9075, 3), new Item(557, 10), new Item(560, 2)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}
			
			@Override
			public MagicSpellbook getSpellbook() {
				return MagicSpellbook.LUNAR;
			}
		}),
		VENGEANCE(new Spell() {

			@Override
			public int spellId() {
				return 30306;
			}

			@Override
			public int levelRequired() {
				return 94;
			}

			@Override
			public int baseExperience() {
				return 14000;
			}

			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.of(new Item[]{new Item(9075, 4), new Item(557, 10), new Item(560, 2)});
			}

			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}

			@Override
			public void startCast(Character cast, Character castOn) {


			}
			
			@Override
			public MagicSpellbook getSpellbook() {
				return MagicSpellbook.LUNAR;
			}
		});

		MagicSpells(Spell spell) {
			this.spell = spell;
		}

		private Spell spell;

		public Spell getSpell() {
			return spell;
		}
	}
	
	public static boolean handleSpell(Player player, int button) {
		switch(button) {
		case VENGEANCE_SPELL_BUTTON:
			if(player.getDueling().inDuel()) {
				player.getPacketSender().sendMessage("You cannot cast Vengeance during a duel!");
				return true;
			}
			if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
				player.getPacketSender().sendMessage("You need at least level 40 Defence to cast this spell.");
				return true;
			}
			if(!MagicSpells.VENGEANCE.getSpell().canCast(player, false)) {
				return true;
			}
			
			if(player.hasVengeance()) {
				player.getPacketSender().sendMessage("You already have Vengeance's effect.");
				return true;
			}
			
			
			if(!player.getVengeanceTimer().finished()) {
				player.getPacketSender().sendMessage("You must wait another "+player.getVengeanceTimer().secondsRemaining()+" seconds before you can cast that again.");
				return true;
			}
			
			//Send message and effect timer to client
			
			player.setHasVengeance(true);
			player.getVengeanceTimer().start(30);
			player.getPacketSender().sendEffectTimer(30, EffectTimer.VENGEANCE)
			.sendMessage("You now have Vengeance's effect.");
			player.getInventory().deleteItemSet(MagicSpells.VENGEANCE.getSpell().itemsRequired(player));
			player.performAnimation(new Animation(4410));
			player.performGraphic(new Graphic(726, GraphicHeight.HIGH));
			return true;
		}
		return false;
	}

	private static final int VENGEANCE_SPELL_BUTTON = 30306;
}
