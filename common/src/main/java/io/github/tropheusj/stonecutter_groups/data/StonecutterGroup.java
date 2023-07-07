package io.github.tropheusj.stonecutter_groups.data;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public record StonecutterGroup(ResourceLocation id, Ingredient ingredient, List<ItemStack> results) {
	private static final String stonecutterType = Objects.requireNonNull(
			BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.STONECUTTER)
	).toString();

	public void decompose(BiConsumer<ResourceLocation, JsonElement> output) {
		String group = id.toLanguageKey();
		for (int i = 0; i < results.size(); i++) {
			ItemStack result = results.get(i);
			ResourceLocation id = this.id.withSuffix("_generated_" + i);
			JsonObject json = makeRecipeJson(group, result);
			output.accept(id, json);
		}
	}

	/**
	 * {@link SingleItemRecipe.Serializer#fromJson(ResourceLocation, JsonObject)}
	 */
	private JsonObject makeRecipeJson(String group, ItemStack result) {
		JsonObject json = new JsonObject();
		json.addProperty("type", stonecutterType);
		json.addProperty("group", group);
		json.add("ingredient", this.ingredient.toJson());
		ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(result.getItem()); // TODO: nbt support for result
		json.addProperty("result", itemId.toString());
		json.addProperty("count", result.getCount());
		return json;
	}
}
