//package me.friendly.exeter.module.impl.toggle.movement.packetfly;
//
//import me.friendly.exeter.module.ModuleType;
//import me.friendly.exeter.module.ToggleableModule;
//import me.friendly.exeter.module.impl.toggle.movement.packetfly.util.Mode;
//import me.friendly.exeter.module.impl.toggle.movement.packetfly.util.Phase;
//import me.friendly.exeter.module.impl.toggle.movement.packetfly.util.TimeVec;
//import me.friendly.exeter.module.impl.toggle.movement.packetfly.util.Type;
//import me.friendly.exeter.properties.EnumProperty;
//import me.friendly.exeter.properties.NumberProperty;
//import me.friendly.exeter.properties.Property;
//import net.minecraft.network.play.client.CPacketEntityAction;
//import net.minecraft.network.play.client.CPacketPlayer;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class PacketFly extends ToggleableModule
//{
//    protected final Property<Mode> mode        =
//            register(new EnumProperty<>("Mode", Mode.Factor));
//    protected final Property<Float> factor     =
//            register(new NumberProperty<>("Factor", 1.0f, 0.0f, 10.0f));
//    protected final Property<Phase> phase      =
//            register(new EnumProperty<>("Phase", Phase.Full));
//    protected final Property<Type> type        =
//            register(new EnumProperty<>("Type", Type.Up));
//    protected final Property<Boolean> antiKick =
//            register(new Property("AntiKick", true));
//    protected final Property<Boolean> answer   =
//            register(new Property("Answer", false));
//    protected final Property<Boolean> bbOffset   =
//            register(new Property("BB-Offset", false));
//    protected final Property<Integer> invalidY =
//            register(new NumberProperty<>("Invalid-Offset", 1337, 0, 1337));
//    protected final Property<Integer> invalids =
//            register(new NumberProperty<>("Invalids", 1, 0, 10));
//    protected final Property<Integer> sendTeleport   =
//            register(new NumberProperty<>("Teleport", 1, 0, 10));
//    protected final Property<Double> concealY    =
//            register(new NumberProperty<>("C-Y", 0.0, -256.0, 256.0));
//    protected final Property<Double> conceal    =
//            register(new NumberProperty<>("C-Multiplier", 1.0, 0.0, 2.0));
//    protected final Property<Double> ySpeed    =
//            register(new NumberProperty<>("Y-Multiplier", 1.0, 0.0, 2.0));
//    protected final Property<Double> xzSpeed =
//            register(new NumberProperty<>("X/Z-Multiplier", 1.0, 0.0, 2.0));
//    protected final Property<Boolean> positionRotation   =
//            register(new Property("Position-Rotation", false));
//    protected final Property<Boolean> elytra =
//            register(new Property("Elytra", false));
//    protected final Property<Boolean> xzJitter =
//            register(new Property("Jitter-XZ", false));
//    protected final Property<Boolean> yJitter =
//            register(new Property("Jitter-Y", false));
//    protected final Property<Boolean> setPos =
//            register(new Property("Set-Pos", false));
//    protected final Property<Boolean> zeroSpeed =
//            register(new Property("Zero-Speed", false));
//    protected final Property<Boolean> zeroY =
//            register(new Property("Zero-Y", false));
//    protected final Property<Boolean> fixPosition =
//            register(new Property("FixPosition", true));
//    protected final Property<Boolean> zeroTeleport =
//            register(new Property("Zero-Teleport", true));
//    protected final Property<Integer> zoomer   =
//            register(new NumberProperty<>("Zoomies", 3, 0, 10));
//
//    protected final Map<Integer, TimeVec> posLooks = new ConcurrentHashMap<>();
//    protected final Set<CPacketPlayer> playerPackets = new HashSet<>();
//    protected final AtomicInteger teleportID = new AtomicInteger();
//    protected Vec3d vecDelServer;
//    protected int packetCounter;
//    protected boolean zoomies;
//    protected float lastFactor;
//    protected int ticks;
//    protected int zoomTimer = 0;
//
//    public PacketFly()
//    {
//        super("PacketFly", new String[]{"packetfly", "phase", "flight"}, 0xffffff, ModuleType.MOVEMENT);
//        this.listeners.add(new ListenerOverlay(this));
//        this.listeners.add(new ListenerBlockPush("packet_fly_block_push_listener"));
//        this.listeners.add(new ListenerMove(this));
//        this.listeners.add(new ListenerTick(this));
//        this.listeners.add(new ListenerPosLook(this));
//        this.listeners.add(new ListenerMotion(this));
//        this.listeners.add(new ListenerWorldClient(this));
//        this.listeners.addAll(new ListenerCPacket(this).getListeners());
//        //this.setData(new PacketFlyData(this));
//    }
//
//    @Override
//    protected void onEnable()
//    {
//        clearValues();
//        if (mc.player == null)
//        {
//            this.disable();
//            return;
//        }
//
//        if (mc.isSingleplayer())
//        {
//            ModuleUtil.disable(this, TextColor.RED
//                    + "Can't enable PacketFly in SinglePlayer!");
//        }
//
//        // teleportID.set(Managers.POSITION.getTeleportID());
//    }
//
//    @Override
//    public String getDisplayInfo()
//    {
//        return mode.getValue().toString();
//    }
//
//    protected void clearValues()
//    {
//        lastFactor = 1.0f;
//        packetCounter = 0;
//        teleportID.set(0);
//        playerPackets.clear();
//        posLooks.clear();
//        vecDelServer = null;
//    }
//
//    protected void onPacketSend(PacketEvent<? extends CPacketPlayer> event)
//    {
//        if (!playerPackets.remove(event.getPacket()))
//        {
//            event.setCancelled(true);
//        }
//    }
//
//    protected boolean isPlayerCollisionBoundingBoxEmpty()
//    {
//        double o = bbOffset.getValue() ? -0.0625 : 0;
//        return !mc.world
//                  .getCollisionBoxes(mc.player,
//                                     mc.player
//                                       .getEntityBoundingBox()
//                                       .expand(o, o, o))
//                  .isEmpty();
//    }
//
//    protected boolean checkPackets(int amount)
//    {
//        if (++this.packetCounter >= amount)
//        {
//            this.packetCounter = 0;
//            return true;
//        }
//
//        return false;
//    }
//
//    protected void sendPackets(double x, double y, double z, boolean confirm)
//    {
//        Vec3d offset = new Vec3d(x, y, z);
//        Vec3d vec = mc.player.getPositionVector().add(offset);
//        vecDelServer = vec;
//        Vec3d oOB = type.getValue().createOutOfBounds(vec, invalidY.getValue());
//
//        if (positionRotation.getValue())
//        {
//            sendCPacket(PacketUtil
//                    .positionRotation(vec.x,
//                                      vec.y,
//                                      vec.z,
//                                      Managers.ROTATION.getServerYaw(),
//                                      Managers.ROTATION.getServerPitch(),
//                                      mc.player.onGround));
//        }
//        else
//        {
//            sendCPacket(PacketUtil.position(vec.x, vec.y, vec.z));
//        }
//
//        double lastX = Managers.POSITION.getX();
//        double lastY = Managers.POSITION.getY();
//        double lastZ = Managers.POSITION.getZ();
//        boolean last = Managers.POSITION.isOnGround();
//
//        for (int i = 0; i < invalids.getValue(); i++)
//        {
//            sendCPacket(PacketUtil.position(oOB.x, oOB.y, oOB.z));
//            oOB = type.getValue().createOutOfBounds(oOB, invalidY.getValue());
//        }
//
//        if (fixPosition.getValue())
//        {
//            Managers.POSITION.set(lastX, lastY, lastZ);
//            Managers.POSITION.setOnGround(last);
//        }
//
//        if (confirm && (zeroTeleport.getValue() || teleportID.get() != 0))
//        {
//            for (int i = 0; i < sendTeleport.getValue(); i++)
//            {
//                sendConfirmTeleport(vec);
//            }
//        }
//
//        if (elytra.getValue())
//        {
//            PacketUtil.sendAction(CPacketEntityAction.Action.START_FALL_FLYING);
//        }
//    }
//
//    protected void sendConfirmTeleport(Vec3d vec)
//    {
//        int id = teleportID.incrementAndGet();
//        PacketUtil.teleport(id);
//        posLooks.put(id, new TimeVec(vec));
//    }
//
//    protected void sendCPacket(CPacketPlayer packet)
//    {
//        playerPackets.add(packet);
//        mc.player.connection.sendPacket(packet);
//    }
//
//}
