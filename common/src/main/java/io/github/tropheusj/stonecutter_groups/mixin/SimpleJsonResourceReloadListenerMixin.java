package io.github.tropheusj.stonecutter_groups.mixin;

import java.util.Map;

import com.google.gson.JsonElement;
import io.github.tropheusj.stonecutter_groups.data.StonecutterGroupLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {
	@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
			at = @At("RETURN"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void addStonecutterGroupData(ResourceManager resourceManager, ProfilerFiller profilerFiller,
										 CallbackInfoReturnable<Map<ResourceLocation, JsonElement>> cir,
										 Map<ResourceLocation, JsonElement> resources) {
		//noinspection ConstantValue
		if ((Object) this instanceof RecipeManager)
			StonecutterGroupLoader.generateGroupJsons(resourceManager, resources);
	}
}
