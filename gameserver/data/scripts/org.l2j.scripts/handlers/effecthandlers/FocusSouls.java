package handlers.effecthandlers;

import org.l2j.gameserver.model.StatsSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.items.instance.Item;
import org.l2j.gameserver.engine.skill.api.Skill;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.SystemMessageId;

import static org.l2j.gameserver.util.GameUtils.isPlayer;

/**
 * Focus Souls effect implementation.
 *
 * @author nBd, Adry_85
 */
public final class FocusSouls extends AbstractEffect {
    public final int charge;

    public FocusSouls(StatsSet params) {
        charge = params.getInt("charge", 0);
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public void instant(Creature effector, Creature effected, Skill skill, Item item) {
        if (!isPlayer(effected) || effected.isAlikeDead()) {
            return;
        }

        final Player target = effected.getActingPlayer();
        final int maxSouls = (int) target.getStats().getValue(Stat.MAX_SOULS, 0);
        if (maxSouls > 0) {
            final int amount = charge;
            if (target.getChargedSouls() < maxSouls) {
                final int count = target.getChargedSouls() + amount <= maxSouls ? amount : maxSouls - target.getChargedSouls();
                target.increaseSouls(count);
            } else {
                target.sendPacket(SystemMessageId.SOUL_CANNOT_BE_INCREASED_ANYMORE);
            }
        }
    }
}