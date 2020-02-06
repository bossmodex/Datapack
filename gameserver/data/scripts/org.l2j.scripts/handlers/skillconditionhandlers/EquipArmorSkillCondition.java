package handlers.skillconditionhandlers;

import org.l2j.gameserver.enums.InventorySlot;
import org.l2j.gameserver.model.StatsSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.items.BodyPart;
import org.l2j.gameserver.model.items.instance.Item;
import org.l2j.gameserver.model.items.type.ArmorType;
import org.l2j.gameserver.model.skills.ISkillCondition;
import org.l2j.gameserver.engine.skill.api.Skill;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.l2j.gameserver.util.GameUtils.isPlayer;

/**
 * @author Sdw
 */
public class EquipArmorSkillCondition implements ISkillCondition {

	public int armorsMask = 0;
	
	public EquipArmorSkillCondition(StatsSet params) {
		final List<ArmorType> armorTypes = params.getEnumList("armorType", ArmorType.class);
		if (nonNull(armorTypes)) {
			for (ArmorType armorType : armorTypes) {
				armorsMask |= armorType.mask();
			}
		}
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target) {
		if (!isPlayer(caster)) {
			return false;
		}
		
		final Inventory inv = caster.getInventory();
		
		// Get the itemMask of the weared chest (if exists)
		final Item chest = inv.getPaperdollItem(InventorySlot.CHEST);
		if (isNull(chest)) {
			return false;
		}

		final int chestMask = chest.getTemplate().getItemMask();
		
		// If chest armor is different from the condition one return false
		if ((armorsMask & chestMask) == 0) {
			return false;
		}
		
		// So from here, chest armor matches conditions
		
		var chestBodyPart = chest.getBodyPart();
		// return True if chest armor is a Full Armor
		if (chestBodyPart == BodyPart.FULL_ARMOR) {
			return true;
		}
		// check legs armor
		final Item legs = inv.getPaperdollItem(InventorySlot.LEGS);
		if (isNull(legs)) {
			return false;
		}
		final int legMask = legs.getTemplate().getItemMask();
		// return true if legs armor matches too
		return (armorsMask & legMask) != 0;
	}
}
