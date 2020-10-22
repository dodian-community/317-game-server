package net.dodian.old.world.entity.impl.player;

import net.dodian.managers.GroupsManager;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.orm.models.Account;
import net.dodian.orm.models.Group;
import net.dodian.orm.models.entities.character.Character;
import net.dodian.orm.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.dodian.old.net.login.LoginResponses.LOGIN_SUCCESSFUL;
import static net.dodian.old.net.login.LoginResponses.NEW_ACCOUNT;

@Component
@Scope("prototype")
public class PlayerLoading {

    private final GroupsManager groupsManager;
    private final CharacterRepository characterRepository;

    @Autowired
    public PlayerLoading(GroupsManager groupsManager, CharacterRepository characterRepository) {
        this.groupsManager = groupsManager;
        this.characterRepository = characterRepository;
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
        Optional<Character> optionalCharacter = characterRepository.findByName(player.getUsername());

        if(optionalCharacter.isEmpty()) {
            return Optional.of(NEW_ACCOUNT);
        }

        Character character = optionalCharacter.get();

        player.setUsername(character.getName());
        player.getPosition().setAs(character.getPosition());
        player.setSpellbook(character.getSpellBook());
        player.getCombat().setFightType(character.getFightType());
        player.getCombat().setAutoRetaliate(character.getAutoRetaliate());
        player.setClanChatName(character.getClanChat());
        player.setTargetTeleportUnlocked(character.getTargetTeleportUnlocked());
        player.setPreserveUnlocked(character.getPreserveUnlocked());
        player.setSpecialPercentage(character.getSpecialPercentage());
        player.setRecoilDamage(character.getRecoilDamage());
        player.setPoisonDamage(character.getPoisonDamage());
        player.getCombat().getPoisonImmunityTimer().start(character.getPoisonImmunity());
        player.getCombat().getFireImmunityTimer().start(character.getFireImmunity());
        player.getCombat().getTeleBlockTimer().start(character.getTeleBlockTimer());
        player.getCombat().getPrayerBlockTimer().start(character.getPrayerBlockTimer());
        player.getTargetSearchTimer().start(character.getTargetSearchTimer());
        player.getSpecialAttackRestore().start(character.getSpecialAttackRestoreTimer());
        player.setSkullTimer(character.getSkullTimer());
        player.setSkullType(character.getSkullType());
        player.setRunning(character.getRunning());
        player.setRunEnergy(character.getRunEnergy());

        player.getInventory().setItems(character.getInventory());
        player.getEquipment().setItems(character.getEquipment());

        player.getAppearance().set(character.getAppearance());

        player.getSkillManager().setSkills(character.getCharacterSkills());

        for(int i = 0; i < player.getBanks().length; i++) {
            if(i == Bank.BANK_SEARCH_TAB_INDEX) {
                continue;
            }

            //player.setBank(i, new Bank(player)).getBank(i).setItems(character.getBanks().get(i).getItems());
        }

        return Optional.of(LOGIN_SUCCESSFUL);
    }
}
