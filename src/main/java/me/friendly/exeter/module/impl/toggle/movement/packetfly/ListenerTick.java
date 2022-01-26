//package me.friendly.exeter.module.impl.toggle.movement.packetfly;
//
//import me.earth.earthhack.impl.event.listeners.ModuleListener;
//
//import java.util.concurrent.TimeUnit;
//
//final class ListenerTick extends ModuleListener<PacketFly, ListenerTick>
//{
//    public ListenerTick(PacketFly module)
//    {
//        super(module, ListenerTick.class);
//    }
//
//    @Override
//    public void invoke(ListenerTick event)
//    {
//        module.posLooks.entrySet().removeIf(entry ->
//                Systeam.currentTimeMillis() - entry.getValue().getTime()
//                        > TimeUnit.SECONDS.toMillis(30L));
//    }
//
//}
