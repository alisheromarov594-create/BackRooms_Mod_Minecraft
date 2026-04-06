package com.backrooms.mod.entity.client;

import com.backrooms.mod.BackroomsMod;
import com.backrooms.mod.entity.MimicEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

/**
 * Рендерер Mimic — использует бипед-модель и свою текстуру.
 */
public class MimicRenderer extends BipedEntityRenderer<MimicEntity, BipedEntityModel<MimicEntity>> {
    private static final Identifier TEXTURE =
            new Identifier(BackroomsMod.MOD_ID, "textures/entity/mimic.png");

    public MimicRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BipedEntityModel<>(ctx.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
        this.shadowRadius = 0.5f;
    }

    @Override
    public Identifier getTexture(MimicEntity entity) {
        return TEXTURE;
    }
}
