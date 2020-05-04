package net.dodian.packets.handlers;

import java.lang.annotation.*;

/**
 * Tells the packet provider to execute this method with the appropriate packet as argument.
 *
 * @author Nozemi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PacketHandler {
}
