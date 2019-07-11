/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import org.l2j.gameserver.model.Clan;
import org.l2j.gameserver.model.StatsSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.items.instance.Item;
import org.l2j.gameserver.model.skills.Skill;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Item Effect: Increase/decrease PK count permanently.
 * @author Nik
 */
public class SendSystemMessageToClan extends AbstractEffect
{
	private final SystemMessage _message;
	
	public SendSystemMessageToClan(StatsSet params)
	{
		final int id = params.getInt("id", 0);
		_message = SystemMessage.getSystemMessage(id);
		
		if (_message == null)
		{
			throw new IllegalArgumentException("SystemMessageId not found for id: " + id);
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		final Player player = effected.getActingPlayer();
		if ((player == null) || (_message == null))
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan != null)
		{
			clan.broadcastToOnlineMembers(_message);
		}
	}
}
