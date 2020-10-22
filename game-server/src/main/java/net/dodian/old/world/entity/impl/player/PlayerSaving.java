package net.dodian.old.world.entity.impl.player;

import net.dodian.old.util.Misc;
import net.dodian.orm.models.entities.character.Character;
import net.dodian.orm.models.entities.character.CharacterAppearance;
import net.dodian.orm.models.entities.character.CharacterSkills;
import net.dodian.orm.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
public  class PlayerSaving {

	private final CharacterRepository characterRepository;

	@Autowired
	public PlayerSaving(CharacterRepository characterRepository) {
		this.characterRepository = characterRepository;
	}

	public void save(Player player) {
		Optional<Character> characterOptional = this.characterRepository.findByName(player.getUsername());
		Character character = this.characterRepository.findByName(player.getUsername()).orElse(new Character());

		character.setName(player.getUsername());
		character.setPosition(player.getPosition());
		character.setSpellBook(player.getSpellbook());
		character.setFightType(player.getCombat().getFightType());

		character.setAppearance(new CharacterAppearance().set(player.getAppearance()));

		CharacterSkills characterSkills = new CharacterSkills(player.getSkillManager().getSkills());
		character.setCharacterSkills(characterSkills);

		this.characterRepository.save(character);

		/*// Create the path and file objects.
		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		}
		try (FileWriter writer = new FileWriter(file)) {

			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.addProperty("username", player.getUsername().trim());
			object.add("position", builder.toJsonTree(player.getPosition()));
			object.addProperty("spell-book", player.getSpellbook().name());
			object.addProperty("fight-type", player.getCombat().getFightType().name());
			object.addProperty("auto-retaliate", player.getCombat().autoRetaliate());
			object.addProperty("xp-locked", player.isExperienceLocked());
			object.addProperty("clanchat", player.getClanChatName());
			object.addProperty("target-teleport", player.isTargetTeleportUnlocked());
			object.addProperty("preserve", player.isPreserveUnlocked());
			object.addProperty("rigour", player.isRigourUnlocked());
			object.addProperty("augury", player.isAuguryUnlocked());
			object.addProperty("has-veng", player.hasVengeance());
			object.addProperty("last-veng", player.getVengeanceTimer().secondsRemaining());
			object.addProperty("running", player.isRunning());
			object.addProperty("openPresetsOnDeath", player.isOpenPresetsOnDeath());
			object.addProperty("run-energy", player.getRunEnergy());
			object.addProperty("spec-percentage", player.getSpecialPercentage());
			object.addProperty("recoil-damage", player.getRecoilDamage());
			object.addProperty("poison-damage", player.getPoisonDamage());

			object.addProperty("poison-immunity", player.getCombat().getPoisonImmunityTimer().secondsRemaining());
			object.addProperty("fire-immunity", player.getCombat().getFireImmunityTimer().secondsRemaining());
			object.addProperty("teleblock-timer", player.getCombat().getTeleBlockTimer().secondsRemaining());
			object.addProperty("prayerblock-timer", player.getCombat().getPrayerBlockTimer().secondsRemaining());
			object.addProperty("target-search-timer", player.getTargetSearchTimer().secondsRemaining());
			object.addProperty("special-attack-restore-timer", player.getSpecialAttackRestore().secondsRemaining());

			object.addProperty("skull-timer", player.getSkullTimer());
			object.addProperty("skull-type", player.getSkullType().name());

			object.addProperty("total-kills", player.getTotalKills());
			object.addProperty("target-kills", player.getTargetKills());
			object.addProperty("normal-kills", player.getNormalKills());
			object.addProperty("killstreak", player.getKillstreak());
			object.addProperty("highest-killstreak", player.getHighestKillstreak());
			object.add("recent-kills", builder.toJsonTree(player.getRecentKills()));
			object.addProperty("deaths", player.getDeaths());
			object.addProperty("points", player.getPoints());
			object.addProperty("amount-donated", player.getAmountDonated());
			object.addProperty("poison-damage", player.getPoisonDamage());
			object.addProperty("blowpipe-scales", player.getBlowpipeScales());

			object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
			object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
			object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
			object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
			object.add("quick-prayers", builder.toJsonTree(player.getQuickPrayers().getPrayers()));

			object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
			object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));

			/** PRESETS **
			object.add("presets", builder.toJsonTree(player.getPresets()));

			/** BANK **
			for(int i = 0; i < player.getBanks().length; i++) {
				if(i == Bank.BANK_SEARCH_TAB_INDEX) {
					continue;
				}
				if(player.getBank(i) != null) {
					object.add("bank-"+i, builder.toJsonTree(player.getBank(i).getValidItems()));
				}
			}

			writer.write(builder.toJson(object));
			writer.close();


		} catch (Exception e) {
			// An error happened while saving.
			Server.getLogger().log(Level.WARNING,
					"An error has occured while saving a character file!", e);
		}*/
	}

	public static boolean playerExists(String p) {
		p = Misc.formatPlayerName(p.toLowerCase());
		return new File("./data/saves/characters/"+p+".json").exists();
	}
}
