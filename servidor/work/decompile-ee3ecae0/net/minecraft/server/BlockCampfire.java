package net.minecraft.server;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;

public class BlockCampfire extends BlockTileEntity implements IBlockWaterlogged {

    protected static final VoxelShape a = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    public static final BlockStateBoolean b = BlockProperties.r;
    public static final BlockStateBoolean c = BlockProperties.y;
    public static final BlockStateBoolean d = BlockProperties.C;
    public static final BlockStateDirection e = BlockProperties.N;
    private static final VoxelShape f = Block.a(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public BlockCampfire(Block.Info block_info) {
        super(block_info);
        this.p((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockCampfire.b, true)).set(BlockCampfire.c, false)).set(BlockCampfire.d, false)).set(BlockCampfire.e, EnumDirection.NORTH));
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if ((Boolean) iblockdata.get(BlockCampfire.b)) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCampfire) {
                TileEntityCampfire tileentitycampfire = (TileEntityCampfire) tileentity;
                ItemStack itemstack = entityhuman.b(enumhand);
                Optional<RecipeCampfire> optional = tileentitycampfire.a(itemstack);

                if (optional.isPresent()) {
                    if (!world.isClientSide && tileentitycampfire.a(entityhuman.abilities.canInstantlyBuild ? itemstack.cloneItemStack() : itemstack, ((RecipeCampfire) optional.get()).e())) {
                        entityhuman.a(StatisticList.INTERACT_WITH_CAMPFIRE);
                        return EnumInteractionResult.SUCCESS;
                    }

                    return EnumInteractionResult.CONSUME;
                }
            }
        }

        return EnumInteractionResult.PASS;
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (!entity.isFireProof() && (Boolean) iblockdata.get(BlockCampfire.b) && entity instanceof EntityLiving && !EnchantmentManager.i((EntityLiving) entity)) {
            entity.damageEntity(DamageSource.FIRE, 1.0F);
        }

        super.a(iblockdata, world, blockposition, entity);
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (iblockdata.getBlock() != iblockdata1.getBlock()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCampfire) {
                InventoryUtils.a(world, blockposition, ((TileEntityCampfire) tileentity).getItems());
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        boolean flag = world.getFluid(blockposition).getType() == FluidTypes.WATER;

        return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockCampfire.d, flag)).set(BlockCampfire.c, this.h(world.getType(blockposition.down())))).set(BlockCampfire.b, !flag)).set(BlockCampfire.e, blockactioncontext.f());
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if ((Boolean) iblockdata.get(BlockCampfire.d)) {
            generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) generatoraccess));
        }

        return enumdirection == EnumDirection.DOWN ? (IBlockData) iblockdata.set(BlockCampfire.c, this.h(iblockdata1)) : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    private boolean h(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.HAY_BLOCK;
    }

    @Override
    public int a(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockCampfire.b) ? super.a(iblockdata) : 0;
    }

    @Override
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockCampfire.a;
    }

    @Override
    public EnumRenderType c(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Override
    public boolean place(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid) {
        if (!(Boolean) iblockdata.get(BlockProperties.C) && fluid.getType() == FluidTypes.WATER) {
            boolean flag = (Boolean) iblockdata.get(BlockCampfire.b);

            if (flag) {
                if (generatoraccess.p_()) {
                    for (int i = 0; i < 20; ++i) {
                        a(generatoraccess.getMinecraftWorld(), blockposition, (Boolean) iblockdata.get(BlockCampfire.c), true);
                    }
                } else {
                    generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                TileEntity tileentity = generatoraccess.getTileEntity(blockposition);

                if (tileentity instanceof TileEntityCampfire) {
                    ((TileEntityCampfire) tileentity).f();
                }
            }

            generatoraccess.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockCampfire.d, true)).set(BlockCampfire.b, false), 3);
            generatoraccess.getFluidTickList().a(blockposition, fluid.getType(), fluid.getType().a((IWorldReader) generatoraccess));
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private Entity a(Entity entity) {
        return (Entity) (entity instanceof EntityFireballFireball ? ((EntityFireballFireball) entity).shooter : (entity instanceof EntityArrow ? ((EntityArrow) entity).getShooter() : null));
    }

    @Override
    public void a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, Entity entity) {
        if (!world.isClientSide) {
            boolean flag = entity instanceof EntityFireballFireball || entity instanceof EntityArrow && entity.isBurning();

            if (flag) {
                Entity entity1 = this.a(entity);
                boolean flag1 = entity1 == null || entity1 instanceof EntityHuman || world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);

                if (flag1 && !(Boolean) iblockdata.get(BlockCampfire.b) && !(Boolean) iblockdata.get(BlockCampfire.d)) {
                    BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();

                    world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockProperties.r, true), 11);
                }
            }
        }

    }

    public static void a(World world, BlockPosition blockposition, boolean flag, boolean flag1) {
        Random random = world.getRandom();
        ParticleType particletype = flag ? Particles.CAMPFIRE_SIGNAL_SMOKE : Particles.CAMPFIRE_COSY_SMOKE;

        world.b(particletype, true, (double) blockposition.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) blockposition.getY() + random.nextDouble() + random.nextDouble(), (double) blockposition.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (flag1) {
            world.addParticle(Particles.SMOKE, (double) blockposition.getX() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), (double) blockposition.getY() + 0.4D, (double) blockposition.getZ() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }

    }

    public static boolean b(World world, BlockPosition blockposition, int i) {
        for (int j = 1; j <= i; ++j) {
            BlockPosition blockposition1 = blockposition.down(j);
            IBlockData iblockdata = world.getType(blockposition1);

            if (i(iblockdata)) {
                return true;
            }

            boolean flag = VoxelShapes.c(BlockCampfire.f, iblockdata.b((IBlockAccess) world, blockposition, VoxelShapeCollision.a()), OperatorBoolean.AND);

            if (flag) {
                IBlockData iblockdata1 = world.getType(blockposition1.down());

                return i(iblockdata1);
            }
        }

        return false;
    }

    private static boolean i(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.CAMPFIRE && (Boolean) iblockdata.get(BlockCampfire.b);
    }

    @Override
    public Fluid a_(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockCampfire.d) ? FluidTypes.WATER.a(false) : super.a_(iblockdata);
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockCampfire.e, enumblockrotation.a((EnumDirection) iblockdata.get(BlockCampfire.e)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockCampfire.e)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCampfire.b, BlockCampfire.c, BlockCampfire.d, BlockCampfire.e);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityCampfire();
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
