/*
 * Copyright © 2019-2020 L2JOrg
 *
 * This file is part of the L2JOrg project.
 *
 * L2JOrg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2JOrg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai.areas.TowerOfInsolence.HeavenlyRift;

import ai.AbstractNpcAI;
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.model.HeavenlyRift;
import org.l2j.gameserver.model.actor.Attackable;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.instance.Monster;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.util.GameUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class DivineAngel extends AbstractNpcAI {
    public DivineAngel() {
        addNpcHateId(20139);
        addKillId(20139);
    }

    @Override
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        if (GlobalVariablesManager.getInstance().getInt("heavenly_rift_level", 0) > 1) {
            if (HeavenlyRift.getAliveNpcCount(npc.getId()) == 0)//Last
            {
                GlobalVariablesManager.getInstance().set("heavenly_rift_complete", GlobalVariablesManager.getInstance().getInt("heavenly_rift_level", 0));
                GlobalVariablesManager.getInstance().set("heavenly_rift_level", 0);
                GlobalVariablesManager.getInstance().set("heavenly_rift_reward", 1);
                HeavenlyRift.getZone().forEachCreature(riftNpc -> {
                    if(GameUtils.isMonster(riftNpc))
                        ((Monster) riftNpc).broadcastSay(ChatType.NPC_SHOUT, NpcStringId.DIVINE_ANGELS_ARE_NOWHERE_TO_BE_SEEN_I_WANT_TO_TALK_TO_THE_PARTY_LEADER);
                }, riftNpc -> GameUtils.isNpc(riftNpc) && riftNpc.getId() == 18004);

            }
        }
        return super.onKill(npc, killer, isSummon);
    }


    @Override
    public boolean onNpcHate(Attackable mob, Player player, boolean isSummon) {

       if (player == null)
            return false;

        if (GameUtils.isPlayable(player)) {

            AtomicBoolean result = new AtomicBoolean(true);
            HeavenlyRift.getZone().forEachCreature(creature -> result.set(false), creature -> GameUtils.isNpc(creature) && creature.getId() == 18004);
            return result.get();

        } else if (player.getId() == 18004)
            return true;

        return super.onNpcHate(mob, player, isSummon);
    }

    public static AbstractNpcAI provider() {
        return new DivineAngel();
    }
}
