package net.dodian.old.world.entity.impl.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.dodian.managers.GroupsManager;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.content.SkillManager;
import net.dodian.old.world.entity.combat.FightType;
import net.dodian.old.world.model.*;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.orm.models.Account;
import net.dodian.orm.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static net.dodian.old.net.login.LoginResponses.LOGIN_SUCCESSFUL;
import static net.dodian.old.net.login.LoginResponses.NEW_ACCOUNT;

@Component
@Scope("prototype")
public class PlayerLoading {

    private final GroupsManager groupsManager;

    @Autowired
    public PlayerLoading(GroupsManager groupsManager) {
        this.groupsManager = groupsManager;
    }

    public void getGroups(Account account, Player player) {
        Optional<Group> group = groupsManager.getGroupById(account.getGroupId());
        group.ifPresent(player::setPrimaryGroup);
        group.ifPresent(player::addGroup);

        for(int memberGroupId : account.getMemberGroupIds()) {
            group = groupsManager.getGroupById(memberGroupId);
            group.ifPresent(player::addGroup);
        }

        if(player.getPrimaryGroup() == null) {
            Optional<Group> optionalGroup = groupsManager.getGroupById(1);
            player.setPrimaryGroup(optionalGroup.orElse(null));
        }
    }

    public Optional<Integer> load(Player player) {
        Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            return Optional.of(NEW_ACCOUNT);
        }

        try (FileReader fileReader = new FileReader(file)) {
            Gson builder = new GsonBuilder().create();
            JsonObject reader = builder.fromJson(fileReader, JsonObject.class);

            if (reader.has("username")) {
                player.setUsername(reader.get("username").getAsString());
            }

            if (reader.has("position")) {
                player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
            }

            if (reader.has("spell-book")) {
                player.setSpellbook(MagicSpellbook.valueOf(reader.get("spell-book").getAsString()));
            }

            if (reader.has("fight-type")) {
                player.getCombat().setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
            }

            if (reader.has("auto-retaliate")) {
                player.getCombat().setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
            }

            if (reader.has("xp-locked")) {
                player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
            }

            if (reader.has("clanchat")) {
                player.setClanChatName(reader.get("clanchat").getAsString());
            }

            if(reader.has("target-teleport")) {
                player.setTargetTeleportUnlocked(reader.get("target-teleport").getAsBoolean());
            }

            if (reader.has("preserve")) {
                player.setPreserveUnlocked(reader.get("preserve").getAsBoolean());
            }

            if (reader.has("rigour")) {
                player.setRigourUnlocked(reader.get("rigour").getAsBoolean());
            }

            if (reader.has("augury")) {
                player.setAuguryUnlocked(reader.get("augury").getAsBoolean());
            }

            if (reader.has("has-veng")) {
                player.setHasVengeance(reader.get("has-veng").getAsBoolean());
            }

            if (reader.has("last-veng")) {
                player.getVengeanceTimer().start(reader.get("last-veng").getAsInt());
            }

            if (reader.has("spec-percentage")) {
                player.setSpecialPercentage(reader.get("spec-percentage").getAsInt());
            }

            if (reader.has("recoil-damage")) {
                player.setRecoilDamage(reader.get("recoil-damage").getAsInt());
            }

            if (reader.has("poison-damage")) {
                player.setPoisonDamage(reader.get("poison-damage").getAsInt());
            }

            if (reader.has("blowpipe-scales")) {
                player.setBlowpipeScales(reader.get("blowpipe-scales").getAsInt());
            }

            if (reader.has("poison-immunity")) {
                player.getCombat().getPoisonImmunityTimer().start(reader.get("poison-immunity").getAsInt());
            }

            if (reader.has("fire-immunity")) {
                player.getCombat().getFireImmunityTimer().start(reader.get("fire-immunity").getAsInt());
            }

            if (reader.has("teleblock-timer")) {
                player.getCombat().getTeleBlockTimer().start(reader.get("teleblock-timer").getAsInt());
            }

            if (reader.has("prayerblock-timer")) {
                player.getCombat().getPrayerBlockTimer().start(reader.get("prayerblock-timer").getAsInt());
            }

            if (reader.has("target-search-timer")) {
                player.getTargetSearchTimer().start(reader.get("target-search-timer").getAsInt());
            }

            if (reader.has("special-attack-restore-timer")) {
                player.getSpecialAttackRestore().start(reader.get("special-attack-restore-timer").getAsInt());
            }

            if (reader.has("skull-timer")) {
                player.setSkullTimer(reader.get("skull-timer").getAsInt());
            }

            if (reader.has("skull-type")) {
                player.setSkullType(SkullType.valueOf(reader.get("skull-type").getAsString()));
            }

            if (reader.has("running")) {
                player.setRunning(reader.get("running").getAsBoolean());
            }

            if (reader.has("openPresetsOnDeath")) {
                player.setOpenPresetsOnDeath(reader.get("openPresetsOnDeath").getAsBoolean());
            }

            if (reader.has("run-energy")) {
                player.setRunEnergy(reader.get("run-energy").getAsInt());
            }
            if(reader.has("total-kills")) {
                player.setTotalKills(reader.get("total-kills").getAsInt());
            }
            if (reader.has("target-kills")) {
                player.setTargetKills(reader.get("target-kills").getAsInt());
            }
            if (reader.has("normal-kills")) {
                player.setNormalKills(reader.get("normal-kills").getAsInt());
            }
            if(reader.has("killstreak")) {
                player.setKillstreak(reader.get("killstreak").getAsInt());
            }
            if(reader.has("highest-killstreak")) {
                player.setHighestKillstreak(reader.get("highest-killstreak").getAsInt());
            }
            if(reader.has("recent-kills")) {
                String[] recentKills = builder.fromJson(
                        reader.get("recent-kills").getAsJsonArray(), String[].class);
                for (String l : recentKills) {
                    player.getRecentKills().add(l);
                }
            }
            if (reader.has("deaths")) {
                player.setDeaths(reader.get("deaths").getAsInt());
            }

            if (reader.has("points")) {
                player.setPoints(reader.get("points").getAsInt());
            }

            if(reader.has("amount-donated")) {
                player.setAmountDonated(reader.get("amount-donated").getAsInt());
            }

            if (reader.has("inventory")) {
                player.getInventory().setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
            }

            if (reader.has("equipment")) {
                player.getEquipment().setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
            }

            if (reader.has("appearance")) {
                player.getAppearance().set(builder.fromJson(
                        reader.get("appearance").getAsJsonArray(), int[].class));
            }

            if (reader.has("skills")) {
                player.getSkillManager().setSkills(builder.fromJson(
                        reader.get("skills"), SkillManager.Skills.class));
            }

            if (reader.has("quick-prayers")) {
                player.getQuickPrayers().setPrayers(builder.fromJson(
                        reader.get("quick-prayers"), PrayerHandler.PrayerData[].class));
            }

            if (reader.has("friends")) {
                long[] friends = builder.fromJson(
                        reader.get("friends").getAsJsonArray(), long[].class);

                for (long l : friends) {
                    player.getRelations().getFriendList().add(l);
                }
            }

            if (reader.has("ignores")) {
                long[] ignores = builder.fromJson(
                        reader.get("ignores").getAsJsonArray(), long[].class);

                for (long l : ignores) {
                    player.getRelations().getIgnoreList().add(l);
                }
            }

            /** PRESETS **/
            if(reader.has("presets")) {
                Presetable[] sets = builder.fromJson(reader.get("presets"), Presetable[].class);
                player.setPresets(sets);
            }

            /** BANKS **/
            for(int i = 0; i < player.getBanks().length; i++) {
                if(i == Bank.BANK_SEARCH_TAB_INDEX) {
                    continue;
                }
                if(reader.has("bank-"+i)) {
                    player.setBank(i, new Bank(player)).getBank(i).addItems(builder.fromJson(reader.get("bank-"+i).getAsJsonArray(), Item[].class), false);
                }
            }

        } catch (Exception e) {
            return Optional.of(LOGIN_SUCCESSFUL);
        }

        return Optional.of(LOGIN_SUCCESSFUL);
    }
}
