package net.dodian.old.world.entity.combat;

import net.dodian.old.world.model.equipment.BonusManager;

/**
 * A collection of constants that each represent a different fighting type.
 * 
 * @author lare96
 */
public enum FightType {

    STAFF_BASH(401, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    STAFF_POUND(406, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    STAFF_FOCUS(406, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    WARHAMMER_POUND(401, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    WARHAMMER_PUMMEL(401, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    WARHAMMER_BLOCK(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    MAUL_POUND(2661, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    MAUL_PUMMEL(2661, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    MAUL_BLOCK(2661, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    ELDER_MAUL_POUND(7516, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    ELDER_MAUL_PUMMEL(7516, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    ELDER_MAUL_BLOCK(7516, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    GRANITE_MAUL_POUND(1665, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    GRANITE_MAUL_PUMMEL(1665, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    GRANITE_MAUL_BLOCK(1665, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    SCYTHE_REAP(414, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    SCYTHE_CHOP(382, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    SCYTHE_JAB(2066, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.CONTROLLED, 0),
    SCYTHE_BLOCK(382, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    BATTLEAXE_CHOP(401, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    BATTLEAXE_HACK(401, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    BATTLEAXE_SMASH(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    BATTLEAXE_BLOCK(401, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    GREATAXE_CHOP(2062, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    GREATAXE_HACK(2062, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    GREATAXE_SMASH(2066, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    GREATAXE_BLOCK(2062, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    CROSSBOW_ACCURATE(4230, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    CROSSBOW_RAPID(4230, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    CROSSBOW_LONGRANGE(4230, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),

    KARILS_CROSSBOW_ACCURATE(2075, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    KARILS_CROSSBOW_RAPID(2075, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    KARILS_CROSSBOW_LONGRANGE(2075, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),

    BALLISTA_ACCURATE(7218, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    BALLISTA_RAPID(7218, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    BALLISTA_LONGRANGE(7218, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    BLOWPIPE_ACCURATE(5061, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    BLOWPIPE_RAPID(5061, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    BLOWPIPE_LONGRANGE(5061, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),

    ABYSSAL_BLUDGEON_CHOP(7054, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    ABYSSAL_BLUDGEON_SLASH(7054, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    ABYSSAL_BLUDGEON_SMASH(7054, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    ABYSSAL_BLUDGEON_BLOCK(7054, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),

    SHORTBOW_ACCURATE(426, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    SHORTBOW_RAPID(426, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    SHORTBOW_LONGRANGE(426, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    LONGBOW_ACCURATE(426, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    LONGBOW_RAPID(426, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    LONGBOW_LONGRANGE(426, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    DAGGER_STAB(400, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE, 0),
    DAGGER_LUNGE(400, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    DAGGER_SLASH(400, 43, 2, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    DAGGER_BLOCK(400, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    DRAGON_DAGGER_STAB(376, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE, 0),
    DRAGON_DAGGER_LUNGE(376, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    DRAGON_DAGGER_SLASH(377, 43, 2, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    DRAGON_DAGGER_BLOCK(376, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    SWORD_STAB(412, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE, 0),
    SWORD_LUNGE(412, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    SWORD_SLASH(451, 43, 2, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    SWORD_BLOCK(412, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    SCIMITAR_CHOP(390, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    SCIMITAR_SLASH(390, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    SCIMITAR_LUNGE(390, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    SCIMITAR_BLOCK(390, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    LONGSWORD_CHOP(451, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    LONGSWORD_SLASH(451, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    LONGSWORD_LUNGE(412, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    LONGSWORD_BLOCK(451, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    MACE_POUND(401, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    MACE_PUMMEL(401, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    MACE_SPIKE(401, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    MACE_BLOCK(401, 43, 3, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    KNIFE_ACCURATE(806, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    KNIFE_RAPID(806, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    KNIFE_LONGRANGE(806, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    OBBY_RING_ACCURATE(2614, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    OBBY_RING_RAPID(2614, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    OBBY_RING_LONGRANGE(2614, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    SPEAR_LUNGE(2080, 43, 0, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    SPEAR_SWIPE(2081, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.CONTROLLED, 0),
    SPEAR_POUND(2082, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.CONTROLLED, 0),
    SPEAR_BLOCK(2080, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    TWOHANDEDSWORD_CHOP(7046, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    TWOHANDEDSWORD_SLASH(7045, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    TWOHANDEDSWORD_SMASH(7054, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    TWOHANDEDSWORD_BLOCK(7055, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    VERACS_FLAIL_POUND(1658, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 0),
    VERACS_FLAIL_PUMMEL(1658, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    VERACS_FLAIL_SPIKE(1658, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    VERACS_FLAIL_BLOCK(1658, 43, 3, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 0),
    PICKAXE_SPIKE(401, 43, 0, BonusManager.ATTACK_STAB, FightStyle.ACCURATE, 0),
    PICKAXE_IMPALE(401, 43, 1, BonusManager.ATTACK_STAB, FightStyle.AGGRESSIVE, 0),
    PICKAXE_SMASH(401, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 0),
    PICKAXE_BLOCK(400, 43, 3, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    CLAWS_CHOP(393, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 0),
    CLAWS_SLASH(393, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    CLAWS_LUNGE(393, 43, 2, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    CLAWS_BLOCK(393, 43, 3, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 0),
    HALBERD_JAB(440, 43, 0, BonusManager.ATTACK_STAB, FightStyle.CONTROLLED, 0),
    HALBERD_SWIPE(440, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.AGGRESSIVE, 0),
    HALBERD_FEND(440, 43, 2, BonusManager.ATTACK_STAB, FightStyle.DEFENSIVE, 0),
    UNARMED_PUNCH(422, 43, 0, BonusManager.ATTACK_CRUSH, FightStyle.ACCURATE, 5860),
    UNARMED_KICK(423, 43, 1, BonusManager.ATTACK_CRUSH, FightStyle.AGGRESSIVE, 5862),
    UNARMED_BLOCK(422, 43, 2, BonusManager.ATTACK_CRUSH, FightStyle.DEFENSIVE, 5861),
    WHIP_FLICK(1658, 43, 0, BonusManager.ATTACK_SLASH, FightStyle.ACCURATE, 12298),
    WHIP_LASH(1658, 43, 1, BonusManager.ATTACK_SLASH, FightStyle.CONTROLLED, 12297),
    WHIP_DEFLECT(1658, 43, 2, BonusManager.ATTACK_SLASH, FightStyle.DEFENSIVE, 12296),
    THROWNAXE_ACCURATE(806, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    THROWNAXE_RAPID(806, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    THROWNAXE_LONGRANGE(806, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    DART_ACCURATE(806, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    DART_RAPID(806, 43, 1, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    DART_LONGRANGE(806, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0),
    JAVELIN_ACCURATE(806, 43, 0, BonusManager.ATTACK_RANGE, FightStyle.ACCURATE, 0),
    JAVELIN_RAPID(806, 43, 2, BonusManager.ATTACK_RANGE, FightStyle.AGGRESSIVE, 0),
    JAVELIN_LONGRANGE(806, 43, 3, BonusManager.ATTACK_RANGE, FightStyle.DEFENSIVE, 0);

    /** The animation this fight type holds. */
    private int animation;

    /** The parent config id. */
    private int parentId;

    /** The child config id. */
    private int childId;

    /** The bonus type. */
    private int bonusType;

    /** The fighting style. */
    private FightStyle style;

    private int button;

    /**
     * Create a new {@link FightType}.
     * 
     * @param animation
     *            the animation this fight type holds.
     * @param parentId
     *            the parent config id.
     * @param childId
     *            the child config id.
     * @param bonusType
     *            the bonus type.
     */
    FightType(int animation, int parentId, int childId, int bonusType,
        FightStyle style, int button) {
        this.animation = animation;
        this.parentId = parentId;
        this.childId = childId;
        this.bonusType = bonusType;
        this.style = style;
        this.button = button;
    }

    /**
     * Gets the animation this fight type holds.
     * 
     * @return the animation.
     */
    public int getAnimation() {
        return animation;
    }

    /**
     * Gets the parent config id.
     * 
     * @return the parent id.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Gets the child config id.
     * 
     * @return the child id.
     */
    public int getChildId() {
        return childId;
    }

    /**
     * Gets the bonus type.
     * 
     * @return the bonus type.
     */
    public int getBonusType() {
        return bonusType;
    }

    /**
     * Gets the fighting style.
     * 
     * @return the fighting style.
     */
    public FightStyle getStyle() {
        return style;
    }

    /**
     * Determines the corresponding bonus for this fight type.
     * 
     * @return the corresponding bonus for this fight type.
     */
    public int getCorrespondingBonus() {
        switch (bonusType) {
        case BonusManager.ATTACK_CRUSH:
            return BonusManager.DEFENCE_CRUSH;
        case BonusManager.ATTACK_MAGIC:
            return BonusManager.DEFENCE_MAGIC;
        case BonusManager.ATTACK_RANGE:
            return BonusManager.DEFENCE_RANGE;
        case BonusManager.ATTACK_SLASH:
            return BonusManager.DEFENCE_SLASH;
        case BonusManager.ATTACK_STAB:
            return BonusManager.DEFENCE_STAB;
        default:
            return BonusManager.DEFENCE_CRUSH;
        }
    }

    public int getButton() {
        return button;
    }
}