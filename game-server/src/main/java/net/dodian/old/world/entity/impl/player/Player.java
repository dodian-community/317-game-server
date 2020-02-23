
package net.dodian.old.world.entity.impl.player;

import lombok.Getter;
import lombok.Setter;
import net.dodian.GameConstants;
import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.CombatPoisonEffect;
import net.dodian.old.engine.task.impl.PlayerDeathTask;
import net.dodian.old.engine.task.impl.RestoreSpecialAttackTask;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.SessionState;
import net.dodian.old.net.packet.PacketSender;
import net.dodian.old.util.FrameUpdater;
import net.dodian.old.util.Misc;
import net.dodian.old.util.Stopwatch;
import net.dodian.old.world.World;
import net.dodian.old.world.content.*;
import net.dodian.old.world.content.PrayerHandler.PrayerData;
import net.dodian.old.world.content.clan.ClanChat;
import net.dodian.old.world.content.clan.ClanChatManager;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.bountyhunter.BountyHunter;
import net.dodian.old.world.entity.combat.magic.Autocasting;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.npc.NpcAggression;
import net.dodian.old.world.model.*;
import net.dodian.old.world.model.container.impl.*;
import net.dodian.old.world.model.dialogue.Dialogue;
import net.dodian.old.world.model.dialogue.DialogueOptions;
import net.dodian.old.world.model.equipment.BonusManager;
import net.dodian.old.world.model.movement.MovementStatus;
import net.dodian.old.world.model.syntax.EnterSyntax;
import net.dodian.orm.models.Group;
import net.dodian.orm.models.definitions.ItemDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("prototype")
public class Player extends Character {
    @Getter @Setter private DialogueOptions dialogueOptions;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private String hostAddress;
    @Getter @Setter private Long longUsername;
    @Getter private final List<Player> localPlayers = new LinkedList<>();
    @Getter private final List<NPC> localNpcs = new LinkedList<>();
    @Getter private final PacketSender packetSender = new PacketSender(this);
    @Getter private final Appearance appearance = new Appearance(this);
    @Getter private final SkillManager skillManager = new SkillManager(this);
    @Getter private final PlayerRelations relations = new PlayerRelations(this);
    @Getter private final ChatMessage chatMessages = new ChatMessage();
    @Getter private final FrameUpdater frameUpdater = new FrameUpdater();
    @Getter private final BonusManager bonusManager = new BonusManager();
    @Getter private final QuickPrayers quickPrayers = new QuickPrayers(this);
    @Getter @Setter private PlayerSession session;
    @Getter @Setter private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    @Getter @Setter private PlayerRights rights = PlayerRights.PLAYER;
    @Getter @Setter private Group primaryGroup;
    @Getter @Setter private List<Group> groups = new ArrayList<>();
    @Getter @Setter private PlayerStatus status = PlayerStatus.NONE;
    @Getter @Setter private ClanChat currentClanChat;
    @Getter @Setter private String clanChatName = "Oak";
    @Getter @Setter private Dialogue dialogue;
    @Getter @Setter private Shop shop;
    @Getter @Setter private int interfaceId = -1, walkableInterfaceId = -1, multiIcon;
    @Getter @Setter private boolean isRunning = true;
    @Getter @Setter private int runEnergy = 100;
    @Getter @Setter private boolean isDying;
    @Getter @Setter private boolean regionChange, allowRegionChangePacket;
    @Getter @Setter private boolean experienceLocked;
    @Getter private final Inventory inventory = new Inventory(this);
    @Getter private final Equipment equipment = new Equipment(this);
    @Getter private final PriceChecker priceChecker = new PriceChecker(this);
    @Getter private ForceMovement forceMovement;
    @Getter @Setter private NPC currentPet;
    @Getter @Setter private int skillAnimation;
    @Getter @Setter private boolean drainingPrayer;
    @Getter @Setter private double prayerPointDrain;
    @Getter private final Stopwatch clickDelay = new Stopwatch();
    @Getter private final Stopwatch lastItemPickup = new Stopwatch();
    @Getter @Setter private WalkToTask walkToTask;
    @Getter @Setter private EnterSyntax enterSyntax;
    @Getter @Setter private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    @Getter private final Stopwatch karambwanTimer = new Stopwatch();
    @Getter private final Stopwatch foodTimer = new Stopwatch();
    @Getter private final Stopwatch potionTimer = new Stopwatch();

    @Getter private final SecondsTimer yellDelay = new SecondsTimer();
    @Getter public final SecondsTimer increaseStats = new SecondsTimer();
    @Getter public final SecondsTimer decreaseStats = new SecondsTimer();

    @Getter @Setter private int destroyItem = -1;
    @Getter @Setter private boolean updateInventory; //Updates inventory on next tick
    @Getter @Setter private boolean queuedAppearanceUpdate; //Updates appearance on next tick
    @Getter @Setter private boolean newPlayer;

    //Combat
    @Getter @Setter private SkullType skullType = SkullType.WHITE_SKULL;
    @Getter private final SecondsTimer aggressionTolerance = new SecondsTimer();
    @Getter @Setter private CombatSpecial combatSpecial;
    @Getter @Setter private int recoilDamage;
    @Getter @Setter private SecondsTimer vengeanceTimer = new SecondsTimer();
    @Getter @Setter private int wildernessLevel;
    @Getter @Setter private int skullTimer;
    @Getter @Setter private int points;
    @Getter @Setter private int amountDonated;

    //Blowpipe
    @Getter @Setter private int blowpipeScales;

    //Delay for restoring special attack
    @Getter private final SecondsTimer specialAttackRestore = new SecondsTimer();

    //Bounty hunter
    @Getter @Setter private int targetKills;
    @Getter @Setter private int normalKills;
    @Getter @Setter private int totalKills;
    @Getter @Setter private int killstreak;
    @Getter @Setter private int highestKillstreak;
    @Getter @Setter private int deaths;
    @Getter @Setter private int safeTimer = 180;
    @Getter private final SecondsTimer targetSearchTimer = new SecondsTimer();
    @Getter private final List<String> recentKills = new ArrayList<String>(); //Contains ip addresses of recent kills

    //Logout
    @Getter private final SecondsTimer forcedLogoutTimer = new SecondsTimer();

    @Getter @Setter private boolean preserveUnlocked;
    @Getter @Setter private boolean rigourUnlocked;
    @Getter @Setter private boolean auguryUnlocked;
    @Getter @Setter private boolean targetTeleportUnlocked;

    //Banking
    @Getter private int currentBankTab;
    @Getter @Setter private Bank[] banks = new Bank[Bank.TOTAL_BANK_TABS]; // last index is for bank searches
    @Getter @Setter private boolean noteWithdrawal, insertMode, searchingBank;
    @Getter @Setter private String searchSyntax = "";

    //Trading
    @Getter private final Trading trading = new Trading(this);
    @Getter private final Dueling dueling = new Dueling(this);

    //Presets
    @Getter @Setter private Presetable currentPreset;
    @Getter @Setter private Presetable[] presets = new Presetable[Presetables.MAX_PRESETS];
    @Getter @Setter private boolean openPresetsOnDeath = true;

    public Player() {
        super(GameConstants.DEFAULT_POSITION.copy());
    }

    @Override
    public void appendDeath() {
        if (!isDying) {
            isDying = true;
            TaskManager.submit(new PlayerDeathTask(this));
        }
    }

    @Override
    public int getHitpoints() {
        return getSkillManager().getCurrentLevel(Skill.HITPOINTS);
    }

    @Override
    public int getAttackAnim() {
        return getCombat().getFightType().getAnimation();
    }


    @Override
    public int getBlockAnim() {
        final Item shield = getEquipment().getItems()[Equipment.SHIELD_SLOT];
        final Item weapon = getEquipment().getItems()[Equipment.WEAPON_SLOT];
        ItemDefinition definition = shield.getId() > 0 ? shield.getDefinition() : weapon.getDefinition();
        return definition.getBlockAnim();
    }

    @Override
    public Character setHitpoints(int hitpoints) {
        if (isDying) {
            return this;
        }

        skillManager.setCurrentLevel(Skill.HITPOINTS, hitpoints);
        packetSender.sendSkill(Skill.HITPOINTS);
        if (getHitpoints() <= 0 && !isDying)
            appendDeath();
        return this;
    }

    @Override
    public void heal(int amount) {
        int level = skillManager.getMaxLevel(Skill.HITPOINTS);
        if ((skillManager.getCurrentLevel(Skill.HITPOINTS) + amount) >= level) {
            setHitpoints(level);
        } else {
            setHitpoints(skillManager.getCurrentLevel(Skill.HITPOINTS) + amount);
        }
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED)
            return skillManager.getCurrentLevel(Skill.RANGED);
        else if (type == CombatType.MAGIC)
            return skillManager.getCurrentLevel(Skill.MAGIC);
        return skillManager.getCurrentLevel(Skill.ATTACK);
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC)
            return skillManager.getCurrentLevel(Skill.MAGIC);
        return skillManager.getCurrentLevel(Skill.DEFENCE);
    }

    @Override
    public int getBaseAttackSpeed() {

        //Gets attack speed for player's weapon
        //If player is using magic, attack speed is
        //Calculated in the MagicCombatMethod class.

        int speed = getCombat().getWeapon().getSpeed();

        if (getCombat().getFightType().toString().toLowerCase().contains("rapid")) {
            speed--;
        }

        return speed;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        return p.getIndex() == getIndex() || p.getUsername().equals(username);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int getSize() {
        return 1;
    }

    public int getAndDecrementSkullTimer() {
        return this.skullTimer--;
    }

    public void onTick() {
        //Process incoming packets...
        getSession().handleQueuedPackets(false);

        //Process walking queue..
        getMovementQueue().onTick();

        //Process walk to task..
        if (walkToTask != null) {
            walkToTask.onTick();
        }

        //Process aggression
        NpcAggression.onTick(this);

        //Process combat
        getCombat().onTick();

        //Process Bounty Hunter
        BountyHunter.onTick(this);

        //Process locations
        Locations.onTick(this);

        //Updates inventory if an update
        //has been requested
        if (isUpdateInventory()) {
            getInventory().refreshItems();
            setUpdateInventory(false);
        }

        //Updates appearance if an update
        //has been requested
        //or if skull timer hits 0.
        if (queuedAppearanceUpdate || getAndDecrementSkullTimer() == 0) {
            getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
            setQueuedAppearanceUpdate(false);
        }


        /**
         * Decrease boosted stats
         * Increase lowered stats
         */
        if (getHitpoints() > 0) {
            if (increaseStats.finished() || decreaseStats.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60)) {
                for (Skill skill : Skill.values()) {
                    int current = getSkillManager().getCurrentLevel(skill);
                    int max = getSkillManager().getMaxLevel(skill);

                    //Should lowered stats be increased?
                    if (current < max) {
                        if (increaseStats.finished()) {
                            int restoreRate = 1;

                            //Rapid restore effect - 2x restore rate for all stats except hp/prayer
                            //Rapid heal - 2x restore rate for hitpoints
                            if (skill != Skill.HITPOINTS && skill != Skill.PRAYER) {
                                if (PrayerHandler.isActivated(this, PrayerHandler.RAPID_RESTORE)) {
                                    restoreRate = 2;
                                }
                            } else if (skill == Skill.HITPOINTS) {
                                if (PrayerHandler.isActivated(this, PrayerHandler.RAPID_HEAL)) {
                                    restoreRate = 2;
                                }
                            }

                            getSkillManager().increaseCurrentLevel(skill, restoreRate, max);
                        }
                    } else if (current > max) {

                        //Should boosted stats be decreased?
                        if (decreaseStats.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60)) {

                            //Never decrease Hitpoints / Prayer
                            if (skill != Skill.HITPOINTS && skill != Skill.PRAYER) {
                                getSkillManager().decreaseCurrentLevel(skill, 1, 1);
                            }

                        }
                    }
                }

                //Reset timers
                if (increaseStats.finished()) {
                    increaseStats.start(60);
                }
                if (decreaseStats.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60)) {
                    decreaseStats.start((PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60));
                }
            }
        }
    }

    public void save() {
        if (session.getState() == SessionState.LOGGED_IN || session.getState() == SessionState.LOGGING_OUT) {
            PlayerSaving.save(this);
        }
    }

    /**
     * Can the player logout?
     *
     * @return Yes if they can logout, false otherwise.
     */
    public boolean canLogout() {
        if (CombatFactory.isBeingAttacked(this)) {
            getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
            return false;
        }
        if (busy()) {
            getPacketSender().sendMessage("You cannot log out at the moment.");
            return false;
        }
        return true;
    }

    /**
     * Sends the logout packet to the client.
     * This results in the ChannelEventHandler
     * adding the player to the logout queue.
     */
    public void logout() {
        getSession().setState(SessionState.REQUESTED_LOG_OUT);
        getPacketSender().sendLogout();
    }

    /**
     * Called by the world's logout queue!
     */
    public void onLogout() {

        //Notify us
        System.out.println("[World] Deregistering player - [username, host] : [" + getUsername() + ", " + getHostAddress() + "]");

        //Update session state
        getSession().setState(SessionState.LOGGING_OUT);

        //If we're in a duel, make sure to give us a loss for logging out.
        if (getDueling().inDuel()) {
            getDueling().duelLost();
        }

        //Do stuff...
        BossPets.pickup(this, getCurrentPet());
        getRelations().updateLists(false);
        BountyHunter.unassign(this);
        getPacketSender().sendLogout();
        getPacketSender().sendInterfaceRemoval();
        ClanChatManager.leave(this, false);
        Locations.logout(this);
        TaskManager.cancelTasks(this);
        save();

        //Send and queue the logout. Also close channel!
        getPacketSender().sendLogout();
        session.setState(SessionState.LOGGED_OUT);
        if (getSession().getChannel().isOpen()) {
            getSession().getChannel().close();
        }
    }

    /**
     * Called by the world's login queue!
     */
    public void onLogin() {
        //Attempt to register the player..
        System.out.println("[World] Registering player - [username, host] : [" + getUsername() + ", " + getHostAddress() + "]");

        //Check if the player is already logged in.. If so, disconnect!!
        Player copy_ = World.getPlayerByName(getUsername());
        if (copy_ != null && copy_.getSession().getState() == SessionState.LOGGED_IN) {
            copy_.logout();
        }

        //Update session state
        getSession().setState(SessionState.LOGGED_IN);

        //Packets
        getPacketSender().sendMapRegion().sendDetails(); //Map region, player index and player rights
        getPacketSender().sendTabs(); //Client sideicons
        getPacketSender().sendMessage("Welcome to Uber Server");

        if(getGroups().stream().anyMatch(Group::isPremium)) {
            getPacketSender().sendMessage("You are a premium member");
        } else {
            getPacketSender().sendMessage("You are not a premium member. To subscribe click 'Become premium' on Dodian.net");
        }

        // TODO: Warning no recoveries set! Go to dodian.com and click 'Account Security' to set them.

        //Send levels and total exp
        long totalExp = 0;
        for (Skill skill : Skill.values()) {
            getSkillManager().updateSkill(skill);
            totalExp += getSkillManager().getExperience(skill);
        }
        getPacketSender().sendTotalExp(totalExp);

        //Send friends and ignored players lists...
        getRelations().setPrivateMessageId(1).onLogin(this).updateLists(true);

        //Reset prayer configs...
        PrayerHandler.resetAll(this);
        getPacketSender().sendConfig(709, PrayerHandler.canUse(this, PrayerData.PRESERVE, false) ? 1 : 0);
        getPacketSender().sendConfig(711, PrayerHandler.canUse(this, PrayerData.RIGOUR, false) ? 1 : 0);
        getPacketSender().sendConfig(713, PrayerHandler.canUse(this, PrayerData.AUGURY, false) ? 1 : 0);

        //Refresh item containers..
        getInventory().refreshItems();
        getEquipment().refreshItems();

        //Interaction options on right click...
        getPacketSender().sendInteractionOption("Follow", 3, false);
        getPacketSender().sendInteractionOption("Trade With", 4, false);

        //Sending run energy attributes...
        getPacketSender().sendRunStatus();
        getPacketSender().sendRunEnergy(getRunEnergy());

        //Sending player's rights..
        getPacketSender().sendRights();

        //Close all interfaces, just in case...
        getPacketSender().sendInterfaceRemoval();

        //Update weapon data and interfaces..
        WeaponInterfaces.assign(this);

        //Update weapon interface configs
        getPacketSender().sendConfig(getCombat().getFightType().getParentId(), getCombat().getFightType().getChildId())
                .sendConfig(172, getCombat().autoRetaliate() ? 1 : 0).updateSpecialAttackOrb();

        //Reset autocasting
        Autocasting.setAutocast(this, null);

        //Update locations..
        Locations.login(this);

        //Update feed
        Feed.sendEntries(this);

        //Update toplist
        Toplist.sendToplist(this);

        //Send pvp stats..
        getPacketSender().
                sendString(52029, "@or1@Killstreak: " + getKillstreak()).
                sendString(52030, "@or1@Kills: " + getTotalKills()).
                sendString(52031, "@or1@Deaths: " + getDeaths()).
                sendString(52033, "@or1@K/D Ratio: " + getKillDeathRatio()).
                sendString(52034, "@or1@Donated: " + getAmountDonated());

        //Join clanchat
        //ClanChatManager.onLogin(this);

        //Handle timers and run tasks
        if (isPoisoned()) {
            TaskManager.submit(new CombatPoisonEffect(this));
        }
        if (getSpecialPercentage() < 100) {
            TaskManager.submit(new RestoreSpecialAttackTask(this));
        }

        if (!getCombat().getFreezeTimer().finished()) {
            getPacketSender().sendEffectTimer(getCombat().getFreezeTimer().secondsRemaining(),
                    EffectTimer.FREEZE);
        }
        if (!getVengeanceTimer().finished()) {
            getPacketSender().sendEffectTimer(getVengeanceTimer().secondsRemaining(),
                    EffectTimer.VENGEANCE);
        }
        if (!getCombat().getFireImmunityTimer().finished()) {
            getPacketSender().sendEffectTimer(getCombat().getFireImmunityTimer().secondsRemaining(),
                    EffectTimer.ANTIFIRE);
        }
        if (!getCombat().getTeleBlockTimer().finished()) {
            getPacketSender().sendEffectTimer(getCombat().getTeleBlockTimer().secondsRemaining(),
                    EffectTimer.TELE_BLOCK);
        }

        decreaseStats.start(60);
        increaseStats.start(60);

        getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
    }

    public void restart(boolean full) {
        performAnimation(new Animation(65535));

        if (full) {
            setSpecialPercentage(100);
            setSkullTimer(0);
            setSkullType(SkullType.WHITE_SKULL);
        } else {
            if (getSpecialPercentage() < 100) {

                if (getSpecialAttackRestore().finished()) {
                    setSpecialPercentage(100);
                    getSpecialAttackRestore().start(120);
                } else {
                    getPacketSender().sendMessage("@red@You must wait another " + getSpecialAttackRestore().secondsRemaining() + " seconds to restore special attack energy.").
                            sendMessage("@red@Don't feel like waiting? Pick up Bluryberry's special from the Emblem trader's shop!");
                }
            }
        }

        setSpecialActivated(false);
        CombatSpecial.updateBar(this);
        setHasVengeance(false);
        getCombat().getFireImmunityTimer().stop();
        getCombat().getPoisonImmunityTimer().stop();
        getCombat().getTeleBlockTimer().stop();
        getCombat().getFreezeTimer().stop();
        getCombat().getPrayerBlockTimer().stop();
        setPoisonDamage(0);
        setWildernessLevel(0);
        setRecoilDamage(0);
        WeaponInterfaces.assign(this);
        BonusManager.update(this);
        PrayerHandler.deactivatePrayers(this);
        getEquipment().refreshItems();
        getInventory().refreshItems();
        for (Skill skill : Skill.values())
            getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
        setRunEnergy(100);
        getMovementQueue().setMovementStatus(MovementStatus.NONE).reset();
        getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
        isDying = false;
        getPacketSender().
                sendEffectTimer(0, EffectTimer.ANTIFIRE).
                sendEffectTimer(0, EffectTimer.FREEZE).
                sendEffectTimer(0, EffectTimer.VENGEANCE).
                sendEffectTimer(0, EffectTimer.TELE_BLOCK);
    }

    public boolean busy() {
        return interfaceId > 0 || isDying || getHitpoints() <= 0 || isNeedsPlacement() || getStatus() != PlayerStatus.NONE;
    }

    /*
     * Fields
     */

    public Player addGroup(Group group) {
        this.groups.add(group);
        return this;
    }

    public Player setForceMovement(ForceMovement forceMovement) {
        this.forceMovement = forceMovement;

        if (forceMovement != null) {
            getUpdateFlag().flag(Flag.FORCED_MOVEMENT);
        }

        return this;
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public Player setCurrentBankTab(int tab) {
        this.currentBankTab = tab;
        return this;
    }

    public boolean withdrawAsNote() {
        return noteWithdrawal;
    }

    public Bank getBank(int index) {
        if (banks[index] == null) {
            banks[index] = new Bank(this);
        }
        return banks[index];
    }

    public Player setBank(int index, Bank bank) {
        this.banks[index] = bank;
        return this;
    }


    public void incrementAmountDonated(int amountDonated) {
        this.amountDonated += amountDonated;
    }

    public void incrementTargetKills() {
        targetKills++;
    }

    public void incrementKills() {
        normalKills++;
    }

    public void incrementTotalKills() {
        this.totalKills++;
    }

    public void incrementDeaths() {
        deaths++;
    }

    public void resetSafingTimer() {
        this.setSafeTimer(180);
    }

    public void incrementKillstreak() {
        this.killstreak++;
    }

    public String getKillDeathRatio() {
        double kc = 0;
        if (deaths == 0) {
            kc = totalKills / 1;
        } else {
            kc = ((double) totalKills / deaths);
        }
        return Misc.FORMATTER.format(kc);
    }

    public boolean isPremium() {
        for (Group group : groups) {
            if (group.isPremium()) {
                return true;
            }
        }

        return false;
    }

    public boolean isStaff() {
        for (Group group : groups) {
            if (group.isStaff()) {
                return true;
            }
        }

        return false;
    }

    public boolean isMaintenance() {
        for (Group group : groups) {
            if (group.isMaintenance()) {
                return true;
            }
        }

        return false;
    }

    public int decrementAndGetBlowpipeScales() {
        return this.blowpipeScales--;
    }

    public int decrementAndGetSafeTimer() {
        return this.safeTimer--;
    }

    public boolean isSkulled() {
        return skullTimer > 0;
    }
}
