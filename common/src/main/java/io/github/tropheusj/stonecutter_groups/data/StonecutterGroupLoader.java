package io.github.tropheusj.stonecutter_groups.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.tropheusj.stonecutter_groups.StonecutterGroups;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class StonecutterGroupLoader {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void generateGroupJsons(ResourceManager manager, Map<ResourceLocation, JsonElement> recipes) {
        Map<ResourceLocation, JsonElement> groupJsons = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(manager, "stonecutter_groups", gson, groupJsons);
        groupJsons.forEach((id, element) -> {
            StonecutterGroup group = loadSafe(id, element);
            if (group != null) {
                group.decompose(recipes::put);
            }
        });
    }

    @Nullable
    private static StonecutterGroup loadSafe(ResourceLocation id, JsonElement element) {
        try {
            return load(id, element.getAsJsonObject());
        } catch (JsonParseException | IllegalStateException e) {
            StonecutterGroups.LOGGER.error("Failed to load stonecutter group: " + id, e);
            return null;
        }
    }

    private static StonecutterGroup load(ResourceLocation id, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"), false);
        if (!(json.get("results") instanceof JsonArray array))
            throw new JsonParseException("results are not an array or are not present");
        List<ItemStack> results = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            ItemStack stack = loadItem(element, "results[" + i + "]");
            results.add(stack);
        }
        return new StonecutterGroup(id, ingredient, results);
    }

    private static ItemStack loadItem(@Nullable JsonElement element, String name) {
        if (element instanceof JsonObject json) {
            CompoundTag nbt = convertJson(json);
            if (!nbt.contains("Count", Tag.TAG_INT))
                nbt.putInt("Count", 1);
            ItemStack stack = ItemStack.of(nbt);
            if (stack.isEmpty())
                throw new JsonParseException(name + " is air or unknown");
            return stack;
        } else if (element instanceof JsonPrimitive primitive && primitive.isString()) {
            JsonObject asObject = new JsonObject();
            asObject.add("id", primitive);
            return loadItem(asObject, name);
        }  else {
            throw new JsonParseException(name + " is not a string or an object: " + element);
        }
    }

    private static CompoundTag convertJson(JsonObject json) {
        try {
            return TagParser.parseTag(json.toString());
        } catch (CommandSyntaxException e) {
            throw new JsonParseException("Invalid NBT: " + json);
        }
    }
}
