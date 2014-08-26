package li.cil.oc.api.event;

import li.cil.oc.api.machine.Robot;
import net.minecraft.entity.Entity;
import cpw.mods.fml.common.eventhandler.Cancelable;

public class RobotAttackEntityEvent extends RobotEvent {
    /**
     * The entity that the robot will attack.
     */
    public final Entity target;

    protected RobotAttackEntityEvent(Robot robot, Entity target) {
        super(robot);
        this.target = target;
    }

    /**
     * Fired when a robot is about to attack an entity.
     * <p/>
     * Canceling this event will prevent the attack.
     */
    @Cancelable
    public static class Pre extends RobotAttackEntityEvent {
        public Pre(Robot robot, Entity target) {
            super(robot, target);
        }
    }

    /**
     * Fired after a robot has attacked an entity.
     */
    public static class Post extends RobotAttackEntityEvent {
        public Post(Robot robot, Entity target) {
            super(robot, target);
        }
    }
}
