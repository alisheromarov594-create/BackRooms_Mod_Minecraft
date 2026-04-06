package com.backrooms.mod.entity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.navigation.PathNavigation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class MimicWatchAndFollowGoal extends Goal {
    private final MimicEntity mimic;
    private final PathNavigation navigation;
    private PlayerEntity trackedPlayer;
    private Vec3d followTarget;
    private int watchTime;
    private int updateCooldown;
    private boolean watching;

    public MimicWatchAndFollowGoal(MimicEntity mimic) {
        this.mimic = mimic;
        this.navigation = mimic.getNavigation();
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.mimic.getTarget() != null) {
            return false;
        }
        this.trackedPlayer = this.mimic.world.getClosestPlayer(this.mimic, 16.0);
        if (this.trackedPlayer == null || this.trackedPlayer.isSpectator() || !this.trackedPlayer.isAlive()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.mimic.getTarget() != null) {
            return false;
        }
        if (this.trackedPlayer == null || !this.trackedPlayer.isAlive()) {
            return false;
        }
        return this.trackedPlayer.squaredDistanceTo(this.mimic) <= 256.0;
    }

    @Override
    public void start() {
        this.updateCooldown = 0;
        this.watchTime = 20 + this.mimic.getRandom().nextInt(41);
        this.watching = this.mimic.getRandom().nextBoolean();
        if (!this.watching) {
            this.updateFollowTarget();
        }
    }

    @Override
    public void stop() {
        this.trackedPlayer = null;
        this.navigation.stop();
    }

    @Override
    public void tick() {
        if (this.trackedPlayer == null) {
            return;
        }

        double distanceSquared = this.trackedPlayer.squaredDistanceTo(this.mimic);
        if (distanceSquared < 12.25) {
            this.mimic.setTarget(this.trackedPlayer);
            return;
        }

        if (this.watching) {
            this.mimic.getLookControl().lookAt(this.trackedPlayer, 30.0f, 30.0f);
            this.navigation.stop();
            if (--this.watchTime <= 0) {
                this.watching = false;
                this.updateFollowTarget();
            }
            return;
        }

        if (this.updateCooldown-- <= 0) {
            this.updateFollowTarget();
            this.updateCooldown = 20;
        }
        if (this.followTarget != null) {
            this.navigation.startMovingTo(this.followTarget.x, this.followTarget.y, this.followTarget.z, 0.1D);
        }

        if (this.mimic.getRandom().nextInt(200) == 0) {
            this.watching = true;
            this.watchTime = 20 + this.mimic.getRandom().nextInt(41);
        }
    }

    private void updateFollowTarget() {
        Vec3d playerPos = this.trackedPlayer.getPos();
        Vec3d velocity = this.trackedPlayer.getVelocity();
        this.followTarget = playerPos.subtract(velocity.multiply(4.0));
    }
}
