package com.playx.gtx.materials;
import com.google.common.collect.ImmutableList;
import com.google.j2objc.annotations.Property;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttribute;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKey;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTElements;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTMaterials.*;
import com.playx.gtx.GTXMod;
import com.playx.gtx.recipes.GTXRecipes;
import com.playx.gtx.recipes.chain.GoldChain;
import com.playx.gtx.recipes.chain.LithiumChain;
import net.minecraft.resources.ResourceLocation;
import com.gregtechceu.gtceu.api.data.chemical.material.Material.Builder;
import org.arbor.gtnn.data.GTNNMaterials;

import static com.google.common.collect.ImmutableList.of;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GTXMaterials {


    public static final MaterialFlag DISABLE_REPLICATION = new MaterialFlag.Builder("disable_replication").requireProps(PropertyKey.INGOT).build();
    public static final MaterialFlag GENERATE_NUCLEAR_COMPOUND = new MaterialFlag.Builder("generate_nuclear_compound").requireProps(PropertyKey.INGOT).build();
    public static final MaterialFlag GENERATE_ROUND = new MaterialFlag.Builder("generate_round").requireProps(PropertyKey.INGOT).build();
    public static final MaterialFlag GENERATE_METAL_CASING = new MaterialFlag.Builder("generate_metal_casing").requireProps(PropertyKey.INGOT).build();
    public static final MaterialFlag SMELT_INTO_FLUID = new MaterialFlag.Builder("smelt_into_fluid").build();
    public static final MaterialFlag GENERATE_ORE = new MaterialFlag.Builder("generate_ore").build();
    public static final MaterialFlag GENERATE_FLUID_BLOCK = new MaterialFlag.Builder("generate_fluid_block").build();


    public static ResourceLocation id(String name) {
        if (GTCEuAPI.materialManager.getMaterial(name) != null) {
            GTXMod.LOGGER.error("Material {} already registered in GTCEU!", name);
        }
        return GTXMod.id(name);
    }

    public static Collection<MaterialFlag> ext2Metal(MaterialFlag... flags) {
        ArrayList<MaterialFlag> list = new ArrayList<>(List.of(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_GEAR, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_LONG_ROD));
        for (MaterialFlag flag : flags) {
            if (list.contains(flag)) {
                continue;
            }

            list.add(flag);
        }

        return list;
    }

    public static Collection<MaterialFlag> coreMetal(MaterialFlag... flags) {
        ArrayList<MaterialFlag> list = new ArrayList<>(List.of(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_GEAR, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_FRAME, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_DENSE));
        for (MaterialFlag flag : flags) {
            if (list.contains(flag)) {
                continue;
            }

            list.add(flag);
        }

        return list;
    }

    private static Material.Builder parseFlags(Material.Builder builder, Collection<MaterialFlag> flags) {
        for (MaterialFlag flag : flags) {
            if (flag == GENERATE_ORE) {
                builder = builder.ore();
            } else if (flag == SMELT_INTO_FLUID) {
                builder = builder.fluid(FluidStorageKeys.MOLTEN, new FluidBuilder().temperature(1200));
            } else if (flag == GENERATE_FLUID_BLOCK) {
                builder = builder.fluid();
            } else {
                builder = builder.flags(flag);
            }
        }

        return builder;
    }

    public static Material DustMaterial(int _metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialFlags) {
        GTXMod.LOGGER.error("Constructing {} material", name);
        return parseFlags(new Material.Builder(id(name)).dust(harvestLevel), materialFlags)

                .color(materialRGB)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material DustMaterial(int _metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialFlags, Element element) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).dust(harvestLevel), materialFlags)

                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material GemMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability) {
        return parseFlags(new Material.Builder(id(name)).gem(harvestLevel), materialGenerationFlags)
                .toolStats(new ToolProperty(toolSpeed, attackDamage, toolDurability, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material GemMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability) {
        return parseFlags(new Material.Builder(id(name)).gem(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(toolSpeed, attackDamage, toolDurability, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material GemMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).gem(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(0, 0, 0, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability, int blastFurnaceTemperature) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(toolSpeed, attackDamage, toolDurability, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW, GTToolType.MINING_HAMMER, GTToolType.MORTAR, GTToolType.DRILL_LV, GTToolType.DRILL_MV}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .blastTemp(blastFurnaceTemperature)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, Element element) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(0, 0, 0, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW, GTToolType.MINING_HAMMER, GTToolType.MORTAR, GTToolType.DRILL_LV, GTToolType.DRILL_MV}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, Element element, int blastFurnaceTemperature) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(0, 0, 0, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW, GTToolType.MINING_HAMMER, GTToolType.MORTAR, GTToolType.DRILL_LV, GTToolType.DRILL_MV}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .componentStacks(materialComponents)
                .blastTemp(blastFurnaceTemperature)
                .buildAndRegister();
    }


    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(toolSpeed, attackDamage, toolDurability, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW, GTToolType.MINING_HAMMER, GTToolType.MORTAR, GTToolType.DRILL_LV, GTToolType.DRILL_MV}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .element(element)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                //.toolStats(new ToolProperty(toolSpeed, attackDamage, toolDurability, harvestLevel, new GTToolType[]{GTToolType.AXE, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.HOE, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW, GTToolType.MINING_HAMMER, GTToolType.MORTAR, GTToolType.DRILL_LV, GTToolType.DRILL_MV}))
                .color(materialRGB)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material IngotMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, Collection<MaterialFlag> materialGenerationFlags) {
        if (harvestLevel > 7) {
            GTXMod.LOGGER.error("OUT OF BOUNDS HARVEST LEVEL {} for {}", harvestLevel, name);
        }
        return parseFlags(new Material.Builder(id(name)).ingot(harvestLevel), materialGenerationFlags)
                .color(materialRGB)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material SimpleFluidMaterial(String name, int rgb) {
        return new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb)
                .buildAndRegister();
        //this(name, rgb, 300, false, "");
    }

    public static Material SimpleFluidMaterial(String name, int rgb, int temperature) {
        return new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(temperature))
                .color(rgb)
                .buildAndRegister();
    }

    // TODO Remove
    public static Material SimpleFluidMaterial(String name, int rgb, ImmutableList<MaterialStack> formula) {
        return new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb)
                .componentStacks(formula)
                .buildAndRegister();
    }

    public static Material SimpleFluidMaterial(String name, int rgb, String formula) {
        return new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb)
                .buildAndRegister()
                .setFormula(formula);
    }

    public static Material SimpleFluidMaterial(String name, int rgb, String formula, boolean fancy) {
        return new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb)
                .buildAndRegister()
                .setFormula(formula);
    }

    public static Material SimpleFluidMaterial(String name, int rgb, boolean hasPlasma) {
        var builder = new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb);
        if (hasPlasma) {
            builder = builder.plasma();
        }

        return builder.buildAndRegister();
    }

    public static Material SimpleFluidMaterial(String name, int rgb, String formula, boolean hasPlasma, boolean fancy) {
        var builder = new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb);
        if (hasPlasma) {
            builder = builder.plasma();
        }

        return builder.buildAndRegister().setFormula(formula);
    }

    public static Material SimpleFluidMaterial(String name, int rgb, int temperature, boolean hasPlasma, String formula) {
        var builder = new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(temperature))
                .color(rgb);
        if (hasPlasma) {
            builder = builder.plasma();
        }

        return builder.buildAndRegister().setFormula(formula);
    }

    public static Material SimpleFluidMaterial(String name, int rgb, boolean hasPlasma, String formula) {
        var builder = new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(300))
                .color(rgb);
        if (hasPlasma) {
            builder = builder.plasma();
        }

        return builder.buildAndRegister();
    }

    public static Material SimpleFluidMaterial(String name, int rgb, int temperature, boolean hasPlasma) {
        var builder = new Builder(id(name))
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(temperature))
                .color(rgb);
        if (hasPlasma) {
            builder = builder.plasma();
        }
        return builder.buildAndRegister();
    }

    public static Material Trinium = new Builder(id("trinium"))
            .ingot()
            .color(0x9aa19c)
            .iconSet(MaterialIconSet.SHINY)
            .blastTemp(8600)
            .element(GTXElements.Trinium)
            .buildAndRegister();
    public static Material Adamantium = new Builder(id("adamantium"))
            .ingot()
            .color(0x2d365c)
            .iconSet(MaterialIconSet.SHINY)
            .blastTemp(10850)
            .element(GTXElements.Adamantium)
            .buildAndRegister();
    public static Material Vibranium = new Builder(id("vibranium"))
            .ingot()
            .color(0x828aad)
            .iconSet(MaterialIconSet.SHINY)
            .blastTemp(11220)
            .element(GTXElements.Vibranium)
            .buildAndRegister();
    public static Material Taranium = new Builder(id("taranium"))
            .ingot()
            .color(0x0c0c0d)
            .iconSet(MaterialIconSet.SHINY)
            .element(GTXElements.Taranium)
            .blastTemp(10000)
            .buildAndRegister();
    public static Material MetastableOganesson = new Builder(id("metastable_oganesson"))
            .ingot()
            .color(0xE61C24)
            .iconSet(MaterialIconSet.SHINY)
            .element(GTElements.Og)
            .blastTemp(10380)
            .buildAndRegister();
    public static Material MetastableFlerovium = new Builder(id("metastable_flerovium"))
            .ingot()
            .color(0x521973)
            .iconSet(MaterialIconSet.SHINY)
            .element(GTElements.Fl)
            .blastTemp(10990)
            .buildAndRegister();
    public static Material MetastableHassium = new Builder(id("metastable_hassium"))
            .ingot()
            .color(0x2d3a9d)
            .iconSet(MaterialIconSet.SHINY)
            .element(GTElements.Hs)
            .blastTemp(11240)
            .buildAndRegister();



    // nuclear materials
    // thorium
    public static Material Thorium232Isotope = IngotMaterial(0, "thorium_232_isotope", Thorium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(new MaterialStack(Thorium, 1)), of(DISABLE_DECOMPOSITION), GTElements.Th, 0);
    public static Material Thorium233 = IngotMaterial(0, "thorium_233", Thorium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(new MaterialStack(Thorium, 1)), of(DISABLE_DECOMPOSITION), GTElements.Th, 0);;

    // Protoactinium
    public static Material Protoactinium233 = IngotMaterial(0, "protoactinium_233", Thorium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.Pr, 0);;

    // Uranium
    public static Material Uranium238Isotope = IngotMaterial(0, "uranium_238_isotope", Uranium238.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.U, 0);;
    public static Material Uranium233 = IngotMaterial(0, "uranium_233", Uranium238.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.U, 0);
    public static Material Uranium234 = IngotMaterial(0, "uranium_234", Uranium238.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.U, 0);
    public static Material Uranium235Isotope = IngotMaterial(0, "uranium_235_isotope", Uranium238.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.U, 0);;
    public static Material Uranium239 = IngotMaterial(0, "uranium_239", Uranium238.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.U, 0);

    // Neptunium
    public static Material Neptunium235 = IngotMaterial(0, "neptunium_235", Neptunium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.Np, 0);
    public static Material Neptunium237 = IngotMaterial(0, "neptunium_237", Neptunium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.Np, 0);
    public static Material Neptunium239 = IngotMaterial(0, "neptunium_239", Neptunium.getMaterialRGB() - 20, MaterialIconSet.SHINY, 0, of(), of(DISABLE_DECOMPOSITION), GTElements.Np, 0);

    // FLUID MATERIALS
    public static Material NeutralMatter = new Builder(id("neutral_matter"))
            .fluid()
            .color(3956968)
            .iconSet(MaterialIconSet.FLUID)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material PositiveMatter = new Builder(id("positive_matter"))
            .fluid()
            .color(11279131)
            .iconSet(MaterialIconSet.FLUID)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material IronChloride = new Builder(id("iron_chloride"))
            .fluid()
            .color( 0x060b0b)
            .components(GTMaterials.Iron, 1, GTMaterials.Chlorine, 3)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material HighPressureSteam = new Builder(id("high_pressure_steam"))
            .gas(new FluidBuilder().temperature(1000))
            .color(0xFFFFFF)
            .iconSet(MaterialIconSet.FLUID)
            .components(Water, 8)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material CoalTarOil = new Builder(id("coal_tar_oil"))
            .color(0xB5B553)
            .fluid()
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.CoalTar, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material SulfuricCoalTarOil = new Builder(id("sulfuric_coal_tar_oil"))
            .color(0xFFFFAD)
            .iconSet(MaterialIconSet.FLUID)
            .fluid()
            .components(CoalTarOil, 1, GTMaterials.SulfuricAcid, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material Anthracene = new Builder(id("antracene"))
            .color(0xA2ACA2)
            .fluid()
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.Carbon, 14, Hydrogen, 10)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material MonoMethyHydrazine = new Builder(id("monomethylhydrazine"))
            .fluid()
            .color(0xFFFFFF)
            .components(GTMaterials.Carbon, 1, Hydrogen, 6, GTMaterials.Nitrogen, 2)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material FermentationBase = new Builder(id("fermentation_base"))
            .color(0x3D5917)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.RareEarth, 1)
            .fluid()
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material LiquidHydrogen = new Builder(id("liquid_hydrogen"))
            .color(0x3AFFC6)
            .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().temperature(14))
            .components(Hydrogen, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material PlatinumConcentrate = new Builder(id("platinum_concentrate"))
            .fluid()
            .iconSet(MaterialIconSet.FLUID)
            .color(GTMaterials.Platinum.getMaterialRGB())
            .components(GTMaterials.Platinum, 1, GTMaterials.RareEarth, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material RhodiumSulfateSolution = new Builder(id("rhodium_sulfate_solution"))
            .fluid()
            .color(0xFFBB66)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.RhodiumSulfate, 1, Water, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material RutheniumTetroxideSolution = new Builder(id("ruthenium_tetroxide_solution"))
            .fluid()
            .color( 0xC7C7C7)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.RutheniumTetroxide, 1, Water, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material HotRhuteniumTetroxideSolution = new Builder(id("hot_ruthenium_tetroxide_solution"))
            .fluid()
            .color( 0xC7C7C7)
            .iconSet(MaterialIconSet.FLUID)
            .components(RutheniumTetroxideSolution, 1, Water, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material OsmiumSolution = new Builder(id("osmium_solution"))
            .fluid()
            .iconSet(MaterialIconSet.FLUID)
            .color((GTMaterials.Osmium.getMaterialRGB() + Water.getMaterialRGB()) / 2)
            .components(GTMaterials.Osmium, 1, Oxygen, 4, Water, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material AcidicOsmiumSolution = new Builder(id("acidic_osmium_solution"))
            .fluid()
            .iconSet(MaterialIconSet.FLUID)
            .color(((GTMaterials.Osmium.getMaterialRGB() + Water.getMaterialRGB()) / 2) - 20)
            .components(GTMaterials.Osmium, 1, Oxygen, 4, Water, 1, GTMaterials.HydrochloricAcid, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material IridiumDioxide = new Builder(id("iridium_dioxide"))
            .dust()
            .color((GTMaterials.Iridium.getMaterialRGB() + Oxygen.getMaterialRGB()) / 2)
            .iconSet(MaterialIconSet.DULL)
            .flags(DISABLE_DECOMPOSITION, MaterialFlags.EXCLUDE_BLOCK_CRAFTING_RECIPES)
            .buildAndRegister();

    public static Material AcidicIridiumSolution = new Builder(id("acidic_iridium_solution"))
            .fluid()
            .color(IridiumDioxide.getMaterialRGB() - 20)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.HydrochloricAcid, 2,IridiumDioxide, 2)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material RhodiumSaltSolution = new Builder(id("rhodium_salt_solution"))
            .fluid()
            .color(0x667788)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.Rhodium, 1, GTMaterials.Salt, 2, GTMaterials.Chlorine, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material RhodiumFilterCakeSolution = new Builder(id("rhodium_filter_cake_solution"))
            .fluid()
            .color(0x667788)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.Rhodium, 2, Water, 2, GTMaterials.RareEarth, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material Deiaminobenzidine = new Builder(id("deiaminobenzidine"))
            .color(0x337D59)
            .fluid()
            .iconSet(MaterialIconSet.DULL)
            .components(GTMaterials.Carbon, 12, Hydrogen, 14, GTMaterials.Nitrogen, 4)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material SodiumTungstate = new Builder(id("sodium_tungstate"))
            .fluid()
            .color(0x7a7777)
            .iconSet(MaterialIconSet.FLUID)
            .components(GTMaterials.Sodium, 2, GTMaterials.Tungsten, 1, Oxygen, 4)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material IodizedOil = new Builder(id("iodized_oil"))
            .color(0x666666)
            .fluid()
            .buildAndRegister();

    // dust materials
    public static Material Pyrotheum = new Builder(id("pyrotheum"))
            .dust()
            .color(0xFF9A3C)
            .iconSet(MaterialIconSet.SAND)
            .components(GTMaterials.Redstone, 1, GTMaterials.Blaze, 2, GTMaterials.Sulfur, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static Material EglinSteelBase = new Builder(id("eglin_steel_base"))
            .dust()
            .color(0x8B4513)
            .iconSet(MaterialIconSet.SAND)
            .components(GTMaterials.Iron, 4, GTMaterials.Kanthal, 1, GTMaterials.Invar, 5)
            .buildAndRegister();

    public static Material MicaBased = new Builder(id("mica_based"))
            .dust()
            .color(0x917445)
            .iconSet(MaterialIconSet.SAND)
            .components(GTMaterials.Mica, 1, GTMaterials.RareEarth, 1)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();

    public static Material AluminoSilicateWool = new Builder(id("alumino_silicate_wool"))
            .dust()
            .color(0xbbbbbb)
            .iconSet(MaterialIconSet.SAND)
            .components(GTMaterials.Aluminium, 2, GTMaterials.Silicon, 1, Oxygen, 5)
            .flags(DISABLE_DECOMPOSITION)
            .buildAndRegister();
    public static final Material Blizz = DustMaterial(951, "blizz", 0x01F3F6, MaterialIconSet.DULL, 1, ImmutableList.of(new MaterialStack(GTMaterials.Redstone, 1), new MaterialStack(Water, 1)), of(NO_SMELTING, SMELT_INTO_FLUID, MORTAR_GRINDABLE));
    public static final Material Snow = DustMaterial(950, "snow", 0xFFFFFF, MaterialIconSet.OPAL, 1, ImmutableList.of(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), of(NO_SMELTING));
    public static final Material Cryotheum = DustMaterial(952, "cryotheum", 0x01F3F6, MaterialIconSet.SAND, 1, ImmutableList.of(new MaterialStack(Redstone, 1), new MaterialStack(Blizz, 2), new MaterialStack(Water, 1)), of(DISABLE_DECOMPOSITION, EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID));
    public static final Material PhthalicAnhydride = DustMaterial(926, "phthalicanhydride", 0xD1D1D1, MaterialIconSet.SAND, 1, of(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material Dibismusthydroborat = DustMaterial(909, "dibismuthhydroborat", 0x00B749, MaterialIconSet.SAND, 2, of(new MaterialStack(Bismuth, 2), new MaterialStack(Hydrogen, 1), new MaterialStack(Boron, 1)), of());
    public static final Material BismuthTellurite = DustMaterial(908, "bismuth_tellurite", 0x006B38, MaterialIconSet.SAND, 2, of(new MaterialStack(Bismuth, 2), new MaterialStack(Tellurium, 3)), of());
    public static final Material CircuitCompoundMK3 = DustMaterial(907, "circuit_compound_mkc", 0x003316, MaterialIconSet.SAND, 2, of(new MaterialStack(IndiumGalliumPhosphide, 1), new MaterialStack(Dibismusthydroborat, 3), new MaterialStack(BismuthTellurite, 2)), of());
    public static final Material YttriumOxide = DustMaterial(906, "yttrium_oxide", 0xC6EBB3, MaterialIconSet.SAND, 2, of(new MaterialStack(Yttrium, 2), new MaterialStack(Oxygen, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material Zirkelite = DustMaterial(904, "zirkelite", 0x6B5E6A, MaterialIconSet.DULL, 2, of(new MaterialStack(Calcium, 2), new MaterialStack(Thorium, 2), new MaterialStack(Cerium, 1), new MaterialStack(Zirconium, 7), new MaterialStack(Rutile, 6), new MaterialStack(Niobium, 4), new MaterialStack(Oxygen, 10)), of(GENERATE_ORE));
    public static final Material PlatinumSalt = DustMaterial(902, "platinum_salt", Platinum.getMaterialRGB(), MaterialIconSet.DULL, 2, of(new MaterialStack(Platinum, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material PlatinumSaltRefined = DustMaterial(901, "refined_platinum_salt", Platinum.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Platinum, 1), new MaterialStack(RareEarth, 1), new MaterialStack(Chlorine, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material PlatinumMetallicPowder = DustMaterial(900, "platinum_metallic_powder", Platinum.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Platinum, 1), new MaterialStack(RareEarth, 1)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material PlatinumResidue = DustMaterial(898, "platinum_residue", 0x64632E, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Iridium, 2), new MaterialStack(RareEarth, 1), new MaterialStack(RareEarth, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material PlatinumRawPowder = DustMaterial(896, "reprecipitated_platinum", Platinum.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Platinum, 1), new MaterialStack(Chlorine, 2)), of(DISABLE_DECOMPOSITION));
    public static final Material PalladiumMetallicPowder = DustMaterial(894, "palladium_metallic_powder", Palladium.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Palladium, 1), new MaterialStack(RareEarth, 1)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material PalladiumRawPowder = DustMaterial(893, "reprecipitated_palladium", Palladium.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Palladium, 1), new MaterialStack(Ammonia, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material PalladiumSalt = DustMaterial(892, "palladium_salt", Palladium.getMaterialRGB(), MaterialIconSet.METALLIC, 2, of(new MaterialStack(Palladium, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material SodiumSulfate = DustMaterial(890, "sodium_sulfate", 0xFFFFFF, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)), of());
    public static final Material PotassiumDisulfate = DustMaterial(888, "potassium_disulfate", 0xFBBB66, MaterialIconSet.DULL, 2, of(new MaterialStack(Potassium, 2), new MaterialStack(Sulfur, 2), new MaterialStack(Oxygen, 7)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID));
    public static final Material LeachResidue = DustMaterial(887, "leach_residue", 0x644629, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Iridium, 2), new MaterialStack(RareEarth, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material CalciumChloride = DustMaterial(884, "calcium_chloride", 0xFFFFFF, MaterialIconSet.DULL, 2, of(new MaterialStack(Calcium, 1), new MaterialStack(Chlorine, 2)), of());
    public static final Material SodiumRuthenate = DustMaterial(882, "sodium_ruthenate", 0x3A40CB, MaterialIconSet.SHINY, 2, of(new MaterialStack(Sodium, 2), new MaterialStack(Oxygen, 4), new MaterialStack(Ruthenium, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material RutheniumTetroxide = DustMaterial(881, "ruthenium_tetroxide", 0xC7C7C7, MaterialIconSet.DULL, 2, of(new MaterialStack(Ruthenium, 1), new MaterialStack(Oxygen, 4)), of(SMELT_INTO_FLUID, EXCLUDE_BLOCK_CRAFTING_RECIPES, DISABLE_DECOMPOSITION));
    public static final Material RarestMetalResidue = DustMaterial(878, "rarest_metal_residue", 0x644629, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Iridium, 2), new MaterialStack(Oxygen, 2), new MaterialStack(SiliconDioxide, 2), new MaterialStack(Gold, 3), new MaterialStack(RareEarth, 1)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material IrMetalResidue = DustMaterial(877, "iridium_metal_residue", 0x846649, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Iridium, 2), new MaterialStack(Oxygen, 4), new MaterialStack(SiliconDioxide, 2), new MaterialStack(Gold, 3)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material PGSDResidue = DustMaterial(876, "sludge_dust_residue", (SiliconDioxide.getMaterialRGB() + Gold.getMaterialRGB())/2, MaterialIconSet.DULL, 2, of(new MaterialStack(SiliconDioxide, 2), new MaterialStack(Gold, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material IridiumChloride = DustMaterial(871, "iridium_chloride", (Iridium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, MaterialIconSet.LAPIS, 2, of(new MaterialStack(Iridium, 1), new MaterialStack(Chlorine, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material PGSDResidue2 = DustMaterial(870, "metallic_sludge_dust_residue", (Copper.getMaterialRGB()+Nickel.getMaterialRGB())/2, MaterialIconSet.DULL, 2, of(new MaterialStack(Copper, 1), new MaterialStack(Nickel, 1)), of(DECOMPOSITION_BY_CENTRIFUGING));
    public static final Material CrudeRhodiumMetal = DustMaterial(868, "crude_rhodium_metal", 0x666666, MaterialIconSet.DULL, 2, of(new MaterialStack(Rhodium, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material SodiumNitrate = DustMaterial(865, "sodium_nitrate", 0x846684, MaterialIconSet.ROUGH, 2, of(new MaterialStack(Sodium, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material RhodiumNitrate = DustMaterial(864, "rhodium_nitrate", (SodiumNitrate.getMaterialRGB()+Rhodium.getMaterialRGB())/2, MaterialIconSet.QUARTZ, 2, of(new MaterialStack(Rhodium, 1), new MaterialStack(Ammonia, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material ZincSulfate = DustMaterial(863, "zinc_sulfate", (Zinc.getMaterialRGB()+Sulfur.getMaterialRGB())/2, MaterialIconSet.QUARTZ, 2, of(new MaterialStack(Zinc, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)), of());
    public static final Material RhodiumFilterCake = DustMaterial(862, "rhodium_filter_cake", RhodiumNitrate.getMaterialRGB()-10, MaterialIconSet.QUARTZ, 2, of(new MaterialStack(Rhodium, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));


    public static final Material PotassiumDichromate = DustMaterial(845, "potassium_dichromate", 0xFF084E, MaterialIconSet.DULL, 0, of(new MaterialStack(Potassium, 2), new MaterialStack(Chromium, 2), new MaterialStack(Oxygen, 7)), of());
    public static final Material Triniite = DustMaterial(836,"triniite",0x5F5A76, MaterialIconSet.SHINY, 7, of(new MaterialStack(Trinium, 3), new MaterialStack(Actinium, 3), new MaterialStack(Selenium, 4), new MaterialStack(Astatine, 4)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material SilverOxide = DustMaterial(835, "silver_oxide", 0x4D4D4D, MaterialIconSet.DULL, 2, of(new MaterialStack(Silver, 2), new MaterialStack(Oxygen, 1)), of());
    public static final Material SilverChloride = DustMaterial(834, "silver_chloride", 0xFEFEFE, MaterialIconSet.DULL, 2, of(new MaterialStack(Silver, 1), new MaterialStack(Chlorine, 1)), of(DISABLE_DECOMPOSITION, GENERATE_FLUID_BLOCK));
    public static final Material PotassiumMetabisulfite = DustMaterial(832, "potassium_metabisulfite", 0xFFFFFF, MaterialIconSet.DULL, 2, of(new MaterialStack(Potassium, 2), new MaterialStack(Sulfur, 2), new MaterialStack(Oxygen, 5)), of());
    public static final Material LeadNitrate = DustMaterial(830, "lead_nitrate", 0xFEFEFE, MaterialIconSet.DULL, 2, of(new MaterialStack(Lead, 1), new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 6)), of());
    public static final Material SodiumPotassiumAlloy = DustMaterial(776, "sodium_potassium_alloy", 0x252525, MaterialIconSet.SHINY, 2, of(new MaterialStack(Sodium, 7), new MaterialStack(Potassium, 3)), of(SMELT_INTO_FLUID));
    public static final Material SodiumFluoride = DustMaterial(773, "sodium_fluoride", (Sodium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, MaterialIconSet.DULL, 2, of(new MaterialStack(Sodium, 1), new MaterialStack(Fluorine, 1)), of());
    public static final Material PotassiumFluoride = DustMaterial(772, "potassium_fluoride", 0xFDFDFD, MaterialIconSet.DULL, 2, of(new MaterialStack(Potassium, 1), new MaterialStack(Fluorine, 1)), of());
    public static final Material FLiNaK = DustMaterial(771, "flinak", 0x252525, MaterialIconSet.DULL, 2, of(new MaterialStack(Fluorine, 3), new MaterialStack(Lithium, 1), new MaterialStack(Sodium, 1), new MaterialStack(Potassium, 1)), of(SMELT_INTO_FLUID));
    public static final Material FLiBe = DustMaterial(769, "flibe", 0x252525, MaterialIconSet.DULL, 2, of(new MaterialStack(Fluorine, 3), new MaterialStack(Lithium, 1), new MaterialStack(Beryllium, 1)), of(SMELT_INTO_FLUID));
    public static final Material OrganicFertilizer = DustMaterial(754, "organic_fertilizer", 0xDDDDDD, MaterialIconSet.SHINY, 2, of(new MaterialStack(Calcium, 5), new MaterialStack(Phosphate, 3), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material CalciumTungstate = DustMaterial(753, "calcium_tungstate", 0x6e6867, MaterialIconSet.SHINY, 0, of(new MaterialStack(Tungsten, 1), new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 4)), of(DISABLE_DECOMPOSITION));
    public static final Material TungsticAcid = DustMaterial(751, "tungstic_acid", 0xFFE700, MaterialIconSet.SHINY, 0, of(new MaterialStack(Hydrogen, 2), new MaterialStack(GTMaterials.Tungsten, 1), new MaterialStack(Oxygen, 4)), of(DISABLE_DECOMPOSITION));
    public static final Material TungstenTrioxide = DustMaterial(750, "tungsten_trioxide", 0x99FF97, GTMaterials.Tungsten.getMaterialIconSet(), 0, of(new MaterialStack(GTMaterials.Tungsten, 1), new MaterialStack(Oxygen, 3)), of(DISABLE_DECOMPOSITION));
    public static final Material TungstenHexachloride = DustMaterial(749, "tungsten_hexachloride", 0x533f75, MaterialIconSet.METALLIC, 0, of(new MaterialStack(GTMaterials.Tungsten, 1), new MaterialStack(GTMaterials.Chlorine, 6)), of(DISABLE_DECOMPOSITION));
    public static final Material NaquadricCompound = DustMaterial(748, "naquadric_compound", GTMaterials.Naquadah.getMaterialRGB(), GTMaterials.Naquadah.getMaterialIconSet(), 0, GTMaterials.Naquadah.getMaterialComponents(), of(GENERATE_ORE));
    public static final Material EnrichedNaquadricCompound = DustMaterial(747, "enriched_naquadric_compound", GTMaterials.NaquadahEnriched.getMaterialRGB(), GTMaterials.NaquadahEnriched.getMaterialIconSet(), 0, GTMaterials.NaquadahEnriched.getMaterialComponents(), of(GENERATE_ORE));
    public static final Material NaquadriaticCompound = DustMaterial(746, "naquadriatic_compound", GTMaterials.Naquadria.getMaterialRGB(), GTMaterials.Naquadria.getMaterialIconSet(), 0, GTMaterials.Naquadria.getMaterialComponents(), of(GENERATE_ORE));
    public static final Material Caliche = DustMaterial(712, "caliche", 0xeb9e3f, MaterialIconSet.DULL, 3, of(new MaterialStack(SodiumNitrate, 1), new MaterialStack(GTMaterials.Potassium, 1), new MaterialStack(GTMaterials.Nitrogen, 1), new MaterialStack(Oxygen, 3), new MaterialStack(GTMaterials.RockSalt, 1), new MaterialStack(GTMaterials.Sodium, 1), new MaterialStack(GTMaterials.Iodine, 1), new MaterialStack(Oxygen, 3)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material Rhodocrosite = DustMaterial(704, "rhodocrosite", 0xff6699, MaterialIconSet.SHINY, 2, of(new MaterialStack(GTMaterials.Manganese, 1), new MaterialStack(GTMaterials.Carbon, 1), new MaterialStack(Oxygen, 3)), of(GENERATE_ORE));
    public static final Material Fluorite = DustMaterial(703, "fluorite", 0x009933, MaterialIconSet.SHINY, 2, of(new MaterialStack(GTMaterials.Calcium, 1), new MaterialStack(GTMaterials.Fluorine, 2)), of(GENERATE_ORE));
    public static final Material Columbite = DustMaterial(702, "columbite", 0xCCCC00, MaterialIconSet.SHINY, 2, of(new MaterialStack(GTMaterials.Iron, 1), new MaterialStack(GTMaterials.Niobium, 2), new MaterialStack(Oxygen, 6)), of(GENERATE_ORE));
    public static final Material FluoroApatite = DustMaterial(689,"fluoroapatite",GTMaterials.Apatite.getMaterialRGB(),MaterialIconSet.DULL,2,of(new MaterialStack(GTMaterials.Calcium, 5), new MaterialStack(GTMaterials.Phosphate, 3), new MaterialStack(GTMaterials.Fluorine, 1)), of(GENERATE_ORE));
    public static final Material NdYAG = DustMaterial(688,"nd_yag",0xcf8acf,MaterialIconSet.SHINY,6,of(), of(SMELT_INTO_FLUID));
    public static final Material PrHoYLF = DustMaterial(687,"prho_ylf",0x6f20af,MaterialIconSet.SHINY,6,of(),of(SMELT_INTO_FLUID));
    public static final Material LuTmYVO = DustMaterial(686,"lutm_yvo",0x206faf,MaterialIconSet.SHINY,6,of(),of(SMELT_INTO_FLUID));
    public static final Material IndiumPhospide = DustMaterial(684,"indium_phosphide",0x5c9c9c,MaterialIconSet.SHINY,6,of(new MaterialStack(GTMaterials.Indium, 1), new MaterialStack(GTMaterials.Phosphorus, 1)),of());
    public static final Material Barytocalcite = DustMaterial(670, "barytocalcite", 0xbf9c7c, MaterialIconSet.SHINY, 2, of (new MaterialStack(GTMaterials.Barium, 1), new MaterialStack(GTMaterials.Calcium, 1), new MaterialStack(GTMaterials.Carbon, 2), new MaterialStack(Oxygen, 6)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material Witherite = DustMaterial(669, "witherite", 0xc6c29d, MaterialIconSet.ROUGH, 2, of(new MaterialStack(GTMaterials.Barium, 1), new MaterialStack(GTMaterials.Carbon, 1), new MaterialStack(Oxygen, 3)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material Arsenopyrite = DustMaterial(668, "arsenopyrite", 0xaa9663, MaterialIconSet.METALLIC, 2, of(new MaterialStack(GTMaterials.Iron, 1), new MaterialStack(GTMaterials.Arsenic, 1), new MaterialStack(GTMaterials.Sulfur, 1)), of(GENERATE_ORE));
    public static final Material Gallite = DustMaterial(667, "gallite", 0x7f7b9e, MaterialIconSet.SHINY, 2, of(new MaterialStack(GTMaterials.Copper, 1), new MaterialStack(GTMaterials.Gallium, 1), new MaterialStack(GTMaterials.Sulfur, 2)), of(GENERATE_ORE));
    public static final Material Bowieite = DustMaterial(666, "bowieite", 0x8b8995, MaterialIconSet.ROUGH, 2, of(new MaterialStack(GTMaterials.Rhodium, 1), new MaterialStack(GTMaterials.Iridium, 1), new MaterialStack(GTMaterials.Platinum, 1), new MaterialStack(GTMaterials.Sulfur, 3)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material Celestine = DustMaterial(665, "celestine", 0x9db1b8, MaterialIconSet.SHINY, 2, of(new MaterialStack(GTMaterials.Strontium, 1), new MaterialStack(GTMaterials.Sulfur, 1), new MaterialStack(Oxygen, 4)), of(GENERATE_ORE));

    // gem materials
    public static final Material CubicZirconia = GemMaterial(911, "cubic_zirconia", 0xFFDFE2, MaterialIconSet.DIAMOND, 6, of(new MaterialStack(Zirconium, 1), new MaterialStack(Oxygen, 2)), of(NO_SMELTING, GENERATE_LENS));
    public static final Material Prasiolite = GemMaterial(910, "prasiolite", 0x9EB749, MaterialIconSet.QUARTZ, 2, of(new MaterialStack(Silicon, 5), new MaterialStack(Oxygen, 10), new MaterialStack(Iron, 1)), of(GENERATE_ORE, MaterialFlags.GENERATE_LENS));
    public static final Material MagnetoResonatic = GemMaterial(913, "magneto_resonatic", 0xFF97FF, MaterialIconSet.MAGNETIC, 2, of(new MaterialStack(Prasiolite, 3), new MaterialStack(BismuthTellurite, 6), new MaterialStack(CubicZirconia, 1), new MaterialStack(SteelMagnetic, 1)), of(DISABLE_DECOMPOSITION, FLAMMABLE, HIGH_SIFTER_OUTPUT, NO_SMELTING, MaterialFlags.GENERATE_LENS));
    public static final Material RhodiumSalt = GemMaterial(867, "rhodium_salt", 0x848484, MaterialIconSet.GEM_VERTICAL, 2, of(new MaterialStack(Rhodium, 1), new MaterialStack(Salt, 2)), of(DISABLE_DECOMPOSITION));
    public static final Material Zircon = GemMaterial(713, "zircon", 0xeb9e3f, MaterialIconSet.GEM_VERTICAL, 3, of(new MaterialStack(Zirconium, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 4)), of(GENERATE_ORE, DISABLE_DECOMPOSITION));
    public static final Material LeadZirconateTitanate = GemMaterial(696, "lead_zirconate_titanate", 0x359ade, MaterialIconSet.OPAL, 3, of(new MaterialStack(Lead, 1), new MaterialStack(Zirconium, 1), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 3)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION));

    // ingot materials
    // INGOT MATERIALS
    public static final Material EglinSteel = IngotMaterial(989, "eglin_steel", 0x8B4513, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(EglinSteelBase, 10), new MaterialStack(Sulfur, 1), new MaterialStack(Silicon, 1), new MaterialStack(Carbon, 1)), ext2Metal(GENERATE_FRAME), null, 1048);
    public static final Material Grisium = IngotMaterial(987, "grisium", 0x355D6A, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Titanium, 9), new MaterialStack(Carbon, 9), new MaterialStack(Potassium, 9), new MaterialStack(Lithium, 9), new MaterialStack(Sulfur, 9), new MaterialStack(Hydrogen, 5)), ext2Metal(GENERATE_FRAME), (Element)null, 3850);
    public static final Material Inconel625 = IngotMaterial(986, "inconel_a", 0x80C880, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Nickel, 3), new MaterialStack(Chromium, 7), new MaterialStack(Molybdenum, 10), new MaterialStack(Invar, 10), new MaterialStack(Nichrome, 13)), ext2Metal(GENERATE_FRAME), null, 2425);
    public static final Material MaragingSteel250 = IngotMaterial(985, "maraging_steel_a", 0x92918D, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Steel, 16), new MaterialStack(Molybdenum, 1), new MaterialStack(Titanium, 1), new MaterialStack(Nickel, 4), new MaterialStack(Cobalt, 2)), ext2Metal(GENERATE_FRAME), null, 2413);
    public static final Material Staballoy = IngotMaterial(983, "staballoy", 0x444B42, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Uranium238, 9), new MaterialStack(Titanium, 1)), ext2Metal(GENERATE_FRAME, DISABLE_DECOMPOSITION), null, 3450);
    public static final Material HastelloyN = IngotMaterial(982, "hastelloy_n", 0xDDDDDD, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Yttrium, 2), new MaterialStack(Molybdenum, 4), new MaterialStack(Chromium, 2), new MaterialStack(Titanium, 2), new MaterialStack(Nickel, 15)), ext2Metal(GENERATE_FRAME, GENERATE_DENSE), null, 4350);
    public static final Material Tumbaga = IngotMaterial(981, "tumbaga", 0xFFB20F, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Gold, 7), new MaterialStack(Bronze, 3)), ext2Metal(GENERATE_FRAME), null, 1200);
    public static final Material Stellite = IngotMaterial(980, "stellite", 0x9991A5, MaterialIconSet.METALLIC, 6, ImmutableList.of(new MaterialStack(Cobalt, 9), new MaterialStack(Chromium, 9), new MaterialStack(Manganese, 5), new MaterialStack(Titanium, 2)), ext2Metal(GENERATE_FRAME), null, 4310);
    public static final Material Talonite = IngotMaterial(979, "talonite", 0x9991A5, MaterialIconSet.SHINY, 6, ImmutableList.of(new MaterialStack(Cobalt, 4), new MaterialStack(Chromium, 3), new MaterialStack(Phosphorus, 2), new MaterialStack(Molybdenum, 1)), ext2Metal(GENERATE_FRAME), null, 3454);
    public static final Material MVSuperconductorBase = IngotMaterial(976, "mv_superconductor_base", 0x535353, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Cadmium, 5), new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 6)), STD_METAL, null, 1200);
    public static final Material HVSuperconductorBase = IngotMaterial(975, "hv_superconductor_base", 0x4a2400, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Titanium, 1), new MaterialStack(Barium, 9), new MaterialStack(Copper, 10), new MaterialStack(Oxygen, 20)), STD_METAL, null, 3300);
    public static final Material EVSuperconductorBase = IngotMaterial(974, "ev_superconductor_base", 0x005800, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Uranium238, 1), new MaterialStack(Platinum, 3)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 4400);
    public static final Material IVSuperconductorBase = IngotMaterial(973, "iv_superconductor_base", 0x300030, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Vanadium, 1), new MaterialStack(Indium, 3)), STD_METAL, null, 5200);
    public static final Material LuVSuperconductorBase = IngotMaterial(972, "luv_superconductor_base", 0x7a3c00, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Indium, 4), new MaterialStack(Bronze, 8), new MaterialStack(Barium, 2), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 14)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 6000);
    public static final Material ZPMSuperconductorBase = IngotMaterial(971, "zpm_superconductor_base", 0x111111, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Naquadah, 4), new MaterialStack(Indium, 2), new MaterialStack(Palladium, 6), new MaterialStack(Osmium, 1)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 8100);
    public static final Material MVSuperconductor = IngotMaterial(970, "mv_superconductor", 0x535353, MaterialIconSet.SHINY, 1, of(new MaterialStack(MVSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material HVSuperconductor = IngotMaterial(969, "hv_superconductor", 0x4a2400, MaterialIconSet.SHINY, 1, of(new MaterialStack(HVSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material EVSuperconductor = IngotMaterial(968, "ev_superconductor", 0x005800, MaterialIconSet.SHINY, 1, of(new MaterialStack(EVSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material IVSuperconductor = IngotMaterial(967, "iv_superconductor", 0x300030, MaterialIconSet.SHINY, 1, of(new MaterialStack(IVSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material LuVSuperconductor = IngotMaterial(966, "luv_superconductor", 0x7a3c00, MaterialIconSet.SHINY, 1, of(new MaterialStack(LuVSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material ZPMSuperconductor = IngotMaterial(964, "zpm_superconductor", 0x111111, MaterialIconSet.SHINY, 1, of(new MaterialStack(ZPMSuperconductorBase, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material Enderium = IngotMaterial(963, "enderium", 0x23524a, MaterialIconSet.METALLIC, 3, ImmutableList.of(new MaterialStack(Lead, 3), new MaterialStack(Platinum, 1), new MaterialStack(EnderPearl, 1)), ext2Metal(DISABLE_DECOMPOSITION), null, 8.0F, 3.0F, 1280, 4500);
    public static final Material Nitinol60 = IngotMaterial(943, "nitinol_a", 0xCCB0EC, MaterialIconSet.METALLIC, 4, ImmutableList.of(new MaterialStack(Nickel, 2), new MaterialStack(Titanium, 3)), ext2Metal(GENERATE_FRAME), null, Titanium.getBlastTemperature());
    public static final Material BabbittAlloy = IngotMaterial(942, "babbitt_alloy", 0xA19CA4, MaterialIconSet.METALLIC, 4, ImmutableList.of(new MaterialStack(Tin, 5), new MaterialStack(Lead, 36), new MaterialStack(Antimony, 8), new MaterialStack(Arsenic, 1)), ext2Metal(GENERATE_FRAME), null, 737);
    public static final Material HG1223 = IngotMaterial(941, "hg_alloy", 0x245397, MaterialIconSet.METALLIC, 4, ImmutableList.of(new MaterialStack(Mercury, 1), new MaterialStack(Barium, 2), new MaterialStack(Calcium, 2), new MaterialStack(Copper, 3), new MaterialStack(Oxygen, 8)), ext2Metal(GENERATE_FRAME, GENERATE_DENSE), null, 5325);
    public static final Material IncoloyMA956 = IngotMaterial(940, "incoloy_ma", 0xAABEBB, MaterialIconSet.METALLIC, 4, ImmutableList.of(new MaterialStack(Iron, 16), new MaterialStack(Aluminium, 3), new MaterialStack(Chromium, 5), new MaterialStack(Yttrium, 1)), ext2Metal(GENERATE_FRAME), null, 5325);
    public static final Material ZirconiumCarbide = IngotMaterial(905, "zirconium_carbide", 0xFFDACD, MaterialIconSet.SHINY, 2, of(new MaterialStack(Zirconium, 1), new MaterialStack(Carbon, 1)), ext2Metal(GENERATE_FRAME), null, 1200);
    public static final Material GoldAlloy = IngotMaterial(828, "gold_alloy", 0xBBA52B, MaterialIconSet.SHINY, 2, of(new MaterialStack(Copper, 3), new MaterialStack(Gold, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION));
    public static final Material PreciousMetal = IngotMaterial(827, "precious_metal", 0xB99023, MaterialIconSet.SHINY, 2, of(new MaterialStack(Gold, 1), new MaterialStack(RareEarth, 1)), of(DISABLE_DECOMPOSITION, GENERATE_ORE), null);
    public static final Material LithiumFluoride = IngotMaterial(774, "lithium_fluoride", 0x757575, MaterialIconSet.SHINY, 2, of(new MaterialStack(Lithium, 1), new MaterialStack(Fluorine, 1)), of()); //LithiumHydroxide + Hydrogen = LithiumFluoride
    public static final Material BerylliumFluoride = IngotMaterial(770, "beryllium_fluoride", 0x757575, MaterialIconSet.SHINY, 2, of(new MaterialStack(Beryllium, 1), new MaterialStack(Fluorine, 2)), of());
    public static final Material LeadBismuthEutectic = IngotMaterial(768, "lead_bismuth_eutatic", 0x757575, MaterialIconSet.SHINY, 2, of(new MaterialStack(Lead, 3), new MaterialStack(Bismuth, 7)), of(SMELT_INTO_FLUID));
    public static final Material AbyssalAlloy = IngotMaterial(755, "abyssal_alloy", 0x9E706A, MaterialIconSet.METALLIC, 6, of(new MaterialStack(StainlessSteel, 5), new MaterialStack(TungstenCarbide, 5), new MaterialStack(Nichrome, 5), new MaterialStack(Bronze, 5), new MaterialStack(IncoloyMA956, 5), new MaterialStack(Iodine, 1), new MaterialStack(Germanium, 1), new MaterialStack(Radon, 1)), ext2Metal(DISABLE_DECOMPOSITION), null, 9625);
    public static final Material ReactorSteel = IngotMaterial(741, "reactor_steel", 0xB4B3B0, MaterialIconSet.SHINY, 2, of(new MaterialStack(Iron, 15), new MaterialStack(Niobium, 1), new MaterialStack(Vanadium, 4), new MaterialStack(Carbon, 2)), of(DISABLE_DECOMPOSITION, GENERATE_DENSE));
    //public static final Material Incoloy813 = IngotMaterial(734, "incoloy813", 0x37bf7e, MaterialIconSet.SHINY, 2, of(new MaterialStack(VanadiumSteel, 4), new MaterialStack(Osmiridium, 2), new MaterialStack(Technetium, 3), new MaterialStack(Germanium, 4), new MaterialStack(Iridium, 7), new MaterialStack(Duranium, 5), new MaterialStack(Californium252, 1)), ext2Metal(GENERATE_FRAME, DISABLE_DECOMPOSITION), null, 10000);
    //public static final Material EnrichedNaquadahAlloy = IngotMaterial(733, "enriched_naquadah_alloy", 0x403f3d, MaterialIconSet.SHINY, 2, of(new MaterialStack(NaquadahEnriched, 4), new MaterialStack(Rhodium, 2), new MaterialStack(Ruthenium, 2), new MaterialStack(Dubnium, 1), new MaterialStack(Rubidium, 2), new MaterialStack(Einsteinium255, 1)), ext2Metal(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_SMALL_GEAR, GENERATE_ROUND, GENERATE_RING, GENERATE_ROTOR), null, 10000);
    // static final Material HastelloyX78 = IngotMaterial(732, "hastelloy_x78", 0x6ba3e3, MaterialIconSet.SHINY, 2, of(new MaterialStack(NaquadahAlloy, 10), new MaterialStack(Rhenium, 5), new MaterialStack(Naquadria, 4), new MaterialStack(Gadolinium, 3), new MaterialStack(Strontium, 2), new MaterialStack(Polonium, 3), new MaterialStack(Rutherfordium, 2), new MaterialStack(Fermium258, 1)), EXT2_METAL | GENERATE_FRAME | DISABLE_DECOMPOSITION | GENERATE_SMALL_GEAR | GENERATE_ROUND | GENERATE_RING | GENERATE_ROTOR, null, 12000);
    //public static final Material HastelloyK243 = IngotMaterial(731, "hastelloy_k243", 0xa5f564, MaterialIconSet.SHINY, 2, of(new MaterialStack(HastelloyX78, 5), new MaterialStack(NiobiumNitride, 2), new MaterialStack(Tritanium, 4), new MaterialStack(TungstenCarbide, 4), new MaterialStack(Promethium, 4), new MaterialStack(Mendelevium261, 1)), EXT2_METAL | GENERATE_FRAME | DISABLE_DECOMPOSITION | GENERATE_SMALL_GEAR | GENERATE_ROUND | GENERATE_RING | GENERATE_ROTOR, null, 12100);
    public static final Material Polyetheretherketone = IngotMaterial(730, "polyetheretherketone", 0x403e37, MaterialIconSet.DULL, 2, of(new MaterialStack(Carbon, 20), new MaterialStack(Hydrogen, 12), new MaterialStack(Oxygen, 3)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID, NO_SMASHING, GENERATE_FOIL, DISABLE_DECOMPOSITION, GENERATE_FINE_WIRE), null);
    public static final Material Zylon = IngotMaterial(729, "zylon", 0xFFE000, MaterialIconSet.SHINY, 2, of(new MaterialStack(Carbon, 14), new MaterialStack(Hydrogen, 6), new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 2)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID, GENERATE_FOIL, NO_SMASHING, DISABLE_DECOMPOSITION), null);
    public static final Material FullerenePolymerMatrix = IngotMaterial(728, "fullerene_polymer_matrix", 0x403e37, MaterialIconSet.DULL, 2, of(new MaterialStack(Palladium, 1),new MaterialStack(Iron,1), new MaterialStack(Carbon, 153), new MaterialStack(Hydrogen, 36), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 2)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID, GENERATE_FOIL, NO_SMASHING, DISABLE_DECOMPOSITION), null);
    public static final Material CarbonNanotubes = IngotMaterial(709, "carbon_nanotubes", 0x2c2c2c, MaterialIconSet.SHINY, 5, of(new MaterialStack(Carbon, 1)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, SMELT_INTO_FLUID, GENERATE_FOIL, DISABLE_DECOMPOSITION, GENERATE_FINE_WIRE), null);
    public static final Material BlackTitanium = IngotMaterial(856, "black_titanium", 0x6C003B, MaterialIconSet.SHINY, 7, of(new MaterialStack(Titanium, 26), new MaterialStack(Lanthanum, 6), new MaterialStack(Tungsten, 4), new MaterialStack(Cobalt, 3), new MaterialStack(Manganese, 2), new MaterialStack(Phosphorus, 2), new MaterialStack(Palladium, 2), new MaterialStack(Niobium, 1), new MaterialStack(Argon, 5)), coreMetal(DISABLE_DECOMPOSITION), null, 11500);
    public static final Material TungstenTitaniumCarbide = IngotMaterial(855, "tungsten_titanium_carbide", 0x800d0d, MaterialIconSet.SHINY, 7, of(new MaterialStack(TungstenCarbide, 7), new MaterialStack(Titanium, 3)), coreMetal(DISABLE_DECOMPOSITION), null, 4422);
    public static final Material TitanSteel = IngotMaterial(854, "titan_steel", 0xAA0d0d, MaterialIconSet.SHINY, 7, of(new MaterialStack(TungstenTitaniumCarbide, 3)), coreMetal(DISABLE_DECOMPOSITION), null, 9200);
    public static final Material Inconel792 = IngotMaterial(853, "inconel_b", 0x6CF076, MaterialIconSet.SHINY, 5, of(new MaterialStack(Nickel, 2), new MaterialStack(Niobium, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Nichrome, 1)), coreMetal(DISABLE_DECOMPOSITION), null, 6200);
    public static final Material Pikyonium = IngotMaterial(852, "pikyonium", 0x3467BA, MaterialIconSet.SHINY, 7, of(new MaterialStack(Inconel792, 8), new MaterialStack(EglinSteel, 5), new MaterialStack(NaquadahEnriched, 4), new MaterialStack(Cerium, 3), new MaterialStack(Antimony, 2), new MaterialStack(Platinum, 2), new MaterialStack(Ytterbium, 1), new MaterialStack(TungstenSteel, 4)), coreMetal(DISABLE_DECOMPOSITION), null, 10400);
    public static final Material Lafium = IngotMaterial(851, "lafium", 0x0d0d60, MaterialIconSet.SHINY, 7, of(new MaterialStack(HastelloyN, 8), new MaterialStack(Naquadah, 4), new MaterialStack(Samarium, 2), new MaterialStack(Tungsten, 4), new MaterialStack(Argon, 2), new MaterialStack(Aluminium, 6), new MaterialStack(Nickel, 8), new MaterialStack(Carbon, 2)), coreMetal(DISABLE_DECOMPOSITION), null, 9865);
    public static final Material Zeron100 = IngotMaterial(850, "zeron", 0xB4B414, MaterialIconSet.SHINY, 5, of(new MaterialStack(Chromium, 13), new MaterialStack(Nickel, 3), new MaterialStack(Molybdenum, 2), new MaterialStack(Copper, 10), new MaterialStack(Tungsten, 2), new MaterialStack(Steel, 20)), coreMetal(DISABLE_DECOMPOSITION), null, 6100);
    public static final Material Cinobite = IngotMaterial(721, "cinobite", 0x010101, MaterialIconSet.SHINY, 5, of(new MaterialStack(Zeron100, 8), new MaterialStack(Naquadria, 4), new MaterialStack(Gadolinium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Mercury, 1), new MaterialStack(Tin, 1), new MaterialStack(Titanium, 6), new MaterialStack(Osmiridium, 1)), coreMetal(DISABLE_DECOMPOSITION), null, 11465);
    //public static final Material HDCS = IngotMaterial(720, "hdcs", 0x334433, MaterialIconSet.SHINY, 5, of(new MaterialStack(TungstenSteel, 12), new MaterialStack(HSSS, 9), new MaterialStack(HSSG, 6), new MaterialStack(Ruridit, 3), new MaterialStack(MagnetoResonatic, 2), new MaterialStack(Plutonium, 1)), coreMetal(DISABLE_DECOMPOSITION | ), null, 9900);
    public static final Material ProtoAdamantium = IngotMaterial(716, "proto_adamantium", 0x4662d4, MaterialIconSet.SHINY, 7, of(new MaterialStack(Adamantium, 3), new MaterialStack(Promethium, 2)), coreMetal(), null, 11244);
    public static final Material TriniumTitanium = IngotMaterial(715, "trinium_titanium", 0x9986a3, MaterialIconSet.SHINY, 7, of(new MaterialStack(Trinium, 2), new MaterialStack(Titanium, 1)), coreMetal(), null, 11000);
    public static final Material LithiumTitanate = IngotMaterial(710, "lithium_titanate", 0xfe71a9, MaterialIconSet.SHINY, 5, of(new MaterialStack(Lithium, 2), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 3)), coreMetal(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 2500);
    public static final Material Titanium50 = IngotMaterial(708, "titanium_50", Titanium.getMaterialRGB(), MaterialIconSet.SHINY, 5, of(), of(), null, 2000);
    public static final Material ElectricallyImpureCopper = IngotMaterial(705, "electrically_impure_copper", 0x765A30, MaterialIconSet.DULL, 2, of(new MaterialStack(Copper, 1), new MaterialStack(RareEarth, 1)), coreMetal(GENERATE_PLATE, DISABLE_DECOMPOSITION));
    public static final Material Polyurethane = IngotMaterial(700, "polyurethane", 0xeffcef, MaterialIconSet.DULL, 2, of(new MaterialStack(Carbon, 17), new MaterialStack(Hydrogen, 16), new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 4)), of(EXCLUDE_BLOCK_CRAFTING_RECIPES, GENERATE_ROD, NO_SMASHING, DISABLE_DECOMPOSITION));
    public static final Material ThoriumDopedTungsten = IngotMaterial(699, "thoria_doped_tungsten", Tungsten.getMaterialRGB(), MaterialIconSet.SHINY, 2, of(new MaterialStack(Thorium, 1), new MaterialStack(Tungsten, 9)), of(GENERATE_ROD, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION));
    public static final Material WoodsGlass = IngotMaterial(698, "woods_glass", 0x730099, MaterialIconSet.SHINY, 2, of(new MaterialStack(SiliconDioxide, 1), new MaterialStack(Barium, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Garnierite, 1), new MaterialStack(SodaAsh, 1)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION));
    public static final Material BariumTitanate = IngotMaterial(697, "barium_titanate", 0x99FF99, MaterialIconSet.SHINY, 2, of(new MaterialStack(Barium, 1), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 3)), of(GENERATE_FOIL, DISABLE_DECOMPOSITION));
    public static final Material TantalumHafniumSeaborgiumCarbide = IngotMaterial(695,"tantalum_hafnium_seaborgium_carbide",0x2c2c2c,MaterialIconSet.SHINY,6,of(new MaterialStack(Tantalum, 12), new MaterialStack(Hafnium, 3), new MaterialStack(Seaborgium, 1), new MaterialStack(Carbon, 16)),of(GENERATE_PLATE, EXCLUDE_BLOCK_CRAFTING_RECIPES, DISABLE_DECOMPOSITION),null,5200);
    public static final Material BismuthRuthenate = IngotMaterial(694,"bismuth_ruthenate", 0x94cf5c, MaterialIconSet.DULL,2, of(new MaterialStack(Bismuth, 2), new MaterialStack(Ruthenium, 2), new MaterialStack(Oxygen, 7)) ,of(GENERATE_PLATE, DISABLE_DECOMPOSITION));
    public static final Material BismuthIridiate = IngotMaterial(693,"bismuth_iridiate", 0x478a6b, MaterialIconSet.DULL,2, of(new MaterialStack(Bismuth, 2), new MaterialStack(Iridium, 2), new MaterialStack(Oxygen, 7)) ,of(GENERATE_PLATE, DISABLE_DECOMPOSITION));
    public static final Material PEDOT = IngotMaterial(692,"pedot", 0x5cef20, MaterialIconSet.DULL,2,of(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2), new MaterialStack(Sulfur, 1)), of(GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION));
    public static final Material RutheniumDioxide = IngotMaterial(691,"ruthenium_dioxide", RutheniumTetroxide.getMaterialRGB(), MaterialIconSet.DULL,2,of(new MaterialStack(Ruthenium, 1), new MaterialStack(Oxygen, 2)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION));
    public static final Material GermaniumTungstenNitride = IngotMaterial(690,"germanium_tungsten_nitride", 0x8f8fcf, MaterialIconSet.DULL,2,of(new MaterialStack(Germanium,3), new MaterialStack(Tungsten,3),new MaterialStack(Nitrogen,10)), of(GENERATE_PLATE), null, 5400);
    public static final Material LithiumNiobate = IngotMaterial(685,"lithium_niobate",0xcfcf3a,MaterialIconSet.SHINY,6,of(new MaterialStack(Lithium, 1), new MaterialStack(Niobium, 1), new MaterialStack(Oxygen, 4)),of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null ,6700);
    public static final Material HeavyQuarkDegenerateMatter = IngotMaterial(682,"heavy_quark_degenerate_matter",0x5dbd3a,MaterialIconSet.SHINY,6,of(),coreMetal(),null,13000);
    public static final Material SuperheavyHAlloy = IngotMaterial(675, "superheavy_h_alloy", 0xE84B36, MaterialIconSet.SHINY, 6, of(new MaterialStack(Copernicium, 1), new MaterialStack(Nihonium, 1), new MaterialStack(MetastableFlerovium, 1), new MaterialStack(Moscovium, 1), new MaterialStack(Livermorium, 1), new MaterialStack(Tennessine,1), new MaterialStack(MetastableOganesson, 1)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 10600);
    public static final Material SuperheavyLAlloy = IngotMaterial(674, "superheavy_l_alloy", 0x2B45DF, MaterialIconSet.SHINY, 6, of(new MaterialStack(Rutherfordium, 1), new MaterialStack(Dubnium, 1), new MaterialStack(Seaborgium, 1), new MaterialStack(Bohrium, 1), new MaterialStack(MetastableHassium, 1), new MaterialStack(Meitnerium,1), new MaterialStack(Roentgenium, 1)), of(GENERATE_PLATE, DISABLE_DECOMPOSITION), null, 10600);
    public static final Material QCDMatter = IngotMaterial(673, "qcd_confined_matter", 0xeb9e3f, MaterialIconSet.SHINY, 7, of(), of(GENERATE_PLATE, DISABLE_REPLICATION, NO_WORKING, NO_SMELTING, NO_SMASHING, GENERATE_FRAME, GENERATE_ROD), null, 13100);
    //public static final Material Periodicium = IngotMaterial(672, "periodicium", 0x3d4bf6, MaterialIconSet.SHINY, 6, of(new MaterialStack(Hydrogen, 1), new MaterialStack(Helium, 1), new MaterialStack(Lithium, 1), new MaterialStack(Beryllium, 1), new MaterialStack(Boron, 1), new MaterialStack(Carbon, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Fluorine, 1), new MaterialStack(Neon, 1), new MaterialStack(Sodium, 1), new MaterialStack(Magnesium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 1), new MaterialStack(Phosphorus, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Chlorine, 1), new MaterialStack(Argon, 1), new MaterialStack(Potassium, 1), new MaterialStack(Calcium, 1), new MaterialStack(Scandium, 1), new MaterialStack(Titanium, 1), new MaterialStack(Vanadium, 1), new MaterialStack(ChromiumTrioxide, 1), new MaterialStack(Manganese, 1), new MaterialStack(Iron, 1), new MaterialStack(Cobalt, 1), new MaterialStack(Nickel, 1), new MaterialStack(Copper, 1), new MaterialStack(Zinc, 1), new MaterialStack(Gallium, 1), new MaterialStack(Germanium, 1), new MaterialStack(Arsenic, 1), new MaterialStack(Selenium, 1), new MaterialStack(Bromine, 1), new MaterialStack(Krypton, 1), new MaterialStack(Rubidium, 1), new MaterialStack(Strontium, 1), new MaterialStack(Yttrium, 1), new MaterialStack(Zirconium, 1), new MaterialStack(Niobium, 1), new MaterialStack(Molybdenum, 1), new MaterialStack(Technetium, 1), new MaterialStack(Ruthenium, 1), new MaterialStack(Rhodium, 1), new MaterialStack(Palladium, 1), new MaterialStack(Silver, 1), new MaterialStack(Cadmium, 1), new MaterialStack(Indium, 1), new MaterialStack(Tin, 1), new MaterialStack(Antimony, 1), new MaterialStack(Tellurium, 1), new MaterialStack(Iodine, 1), new MaterialStack(Xenon, 1), new MaterialStack(Caesium, 1), new MaterialStack(Barium, 1), new MaterialStack(Lanthanum, 1), new MaterialStack(Cerium, 1), new MaterialStack(Praseodymium, 1), new MaterialStack(Neodymium, 1), new MaterialStack(Promethium, 1), new MaterialStack(Samarium, 1), new MaterialStack(Europium, 1), new MaterialStack(Gadolinium, 1), new MaterialStack(Terbium, 1), new MaterialStack(Dysprosium, 1), new MaterialStack(Holmium, 1), new MaterialStack(Erbium, 1), new MaterialStack(Thulium, 1), new MaterialStack(Ytterbium, 1), new MaterialStack(Lutetium, 1), new MaterialStack(Hafnium, 1), new MaterialStack(Tantalum, 1), new MaterialStack(Tungsten, 1), new MaterialStack(Rhenium, 1), new MaterialStack(Osmium, 1), new MaterialStack(Iridium, 1), new MaterialStack(Platinum, 1), new MaterialStack(Gold, 1), new MaterialStack(Mercury, 1), new MaterialStack(Thallium, 1), new MaterialStack(Lead, 1), new MaterialStack(Bismuth, 1), new MaterialStack(Polonium, 1), new MaterialStack(Astatine, 1), new MaterialStack(Radon, 1), new MaterialStack(Francium, 1), new MaterialStack(Radium, 1), new MaterialStack(Actinium, 1), new MaterialStack(Thorium, 1), new MaterialStack(Protactinium, 1), new MaterialStack(Uranium238, 1), new MaterialStack(Neptunium, 1), new MaterialStack(Plutonium, 1), new MaterialStack(Americium, 1), new MaterialStack(Curium, 1), new MaterialStack(Berkelium, 1), new MaterialStack(Californium, 1), new MaterialStack(Einsteinium, 1), new MaterialStack(Fermium, 1), new MaterialStack(Mendelevium, 1), new MaterialStack(Rutherfordium, 1), new MaterialStack(Dubnium, 1), new MaterialStack(Seaborgium, 1), new MaterialStack(Bohrium, 1), new MaterialStack(MetastableHassium, 1), new MaterialStack(Meitnerium, 1), new MaterialStack(Roentgenium, 1), new MaterialStack(Copernicium, 1), new MaterialStack(Nihonium, 1), new MaterialStack(MetastableFlerovium, 1), new MaterialStack(Moscovium, 1), new MaterialStack(Livermorium, 1), new MaterialStack(Tennessine, 1), new MaterialStack(MetastableOganesson, 1)), of(DISABLE_DECOMPOSITION), null, 13500);
    public static final Material CosmicNeutronium = IngotMaterial(671,"cosmic_neutronium",0x323232,MaterialIconSet.SHINY,7,of(new MaterialStack(Neutronium, 1)), coreMetal(DISABLE_DECOMPOSITION),null,14100);
    public static final Material NaquadriaticTaranium = IngotMaterial(775, "naquadriatic_taranium", (Naquadria.getMaterialRGB()+Taranium.getMaterialRGB()) / 2, MaterialIconSet.SHINY, 1, ImmutableList.of(new MaterialStack(Naquadria, 1), new MaterialStack(Taranium, 1)), of(GENERATE_PLATE, GENERATE_LONG_ROD), null, 11200);
    public static final Material Polyimide = IngotMaterial(992, "polyimide", 0xFF7F50, MaterialIconSet.DULL, 1, of(new MaterialStack(Carbon, 22), new MaterialStack(Hydrogen, 12), new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 6)), of(GENERATE_PLATE, FLAMMABLE, NO_SMASHING, SMELT_INTO_FLUID, DISABLE_DECOMPOSITION));
    public static final Material FluorinatedEthylenePropylene = IngotMaterial(988, "fluorinated_ethylene_propylene", 0xC8C8C8, MaterialIconSet.DULL, 1, of(new MaterialStack(Carbon, 5), new MaterialStack(Fluorine, 10)), of(GENERATE_PLATE, FLAMMABLE, NO_SMASHING, SMELT_INTO_FLUID, DISABLE_DECOMPOSITION));


    // Simple fluids
    public static final Material NaquadricSolution = SimpleFluidMaterial("naquadric_solution", 0x232225, "NqNO2");
    public static final Material EnrichedNaquadricSolution = SimpleFluidMaterial("enriched_naquadric_solution", 0x312735, "Nq+NO2");
    public static final Material NaquadriaticSolution = SimpleFluidMaterial("naquadriatic_solution", 0x312735, "*Nq*NO2");
    public static final Material AntimonyPentafluoride = SimpleFluidMaterial("antimony_pentafluoride", Antimony.getMaterialRGB(), "SbF5");
    public static final Material FluoronaquadricAcid = SimpleFluidMaterial("fluoronaquadric_acid", 0x485d60, "H2NqF4");
    public static final Material EnrichedFluoronaquadricAcid = SimpleFluidMaterial("enriched_fluoronaquadric_acid", 0x485d60, "H2Nq+F4");
    public static final Material FluoronaquadriaticAcid = SimpleFluidMaterial("fluoronaquadriatic_acid", 0x485d60, "H2*Nq*F4");
    public static final Material NaquadahDifluoride = SimpleFluidMaterial("naquadah_difluoride", 0x324649, "NqF2");
    public static final Material EnrichedNaquadahDifluoride = SimpleFluidMaterial("enriched_naquadah_difluoride", 0x141e1f, "Nq+F2");
    public static final Material NaquadriaDifluoride = SimpleFluidMaterial("naquadria_difluoride", 0x141e1f, "*Nq*F2");
    public static final Material NaquadriaHexafluoride = SimpleFluidMaterial("naquadria_hexafluoride", 0x111c27, "*Nq*F6");
    public static final Material RadonDifluoride = SimpleFluidMaterial("radon_difluoride", 0x9966ff, "RnF2");
    public static final Material RadonNaquadriaoctafluoride = SimpleFluidMaterial("radon_naquadriaoctafluoride", 0x111c27, "Rd*Nq*F8");
    public static final Material XenonTrioxide = SimpleFluidMaterial("xenon_trioxide", 0x432791, "XeO3");
    public static final Material CesiumFluoride = SimpleFluidMaterial("cesium_fluoride", 0xabab69, "CsF");
    public static final Material CesiumXenontrioxideFluoride = SimpleFluidMaterial("cesium_xenontrioxide_fluoride", 0x3333cc, "CsXeO3F");
    public static final Material RadonTrioxide = SimpleFluidMaterial("radon_trioxide", 0x9966ff, "RnO3");
    public static final Material NaquadriaCesiumXenonNonfluoride = SimpleFluidMaterial("naquadria_cesium_xenon_nonfluoride", 0x1c1c5e, "*Nq*CsXeF9");
    public static final Material NitrylFluoride = SimpleFluidMaterial("nitryl_fluoride", NitricOxide.getMaterialRGB(), "NO2F");
    public static final Material NitrosoniumOctafluoroxenate = SimpleFluidMaterial("nitrosonium_octafluoroxenate", 0x3f3f83, "(NO2)2XeF8");
    public static final Material NaquadriaCesiumfluoride = SimpleFluidMaterial("naquadria_cesiumfluoride", 0x636379, "*Nq*F2CsF");
    public static final Material EnrichedNaquadahhexafluoride = SimpleFluidMaterial("enriched_naquadahhexafluoride", 0x030330, "Nq+F6");
    public static final Material EnrichedXenonHexafluoronaquadate = SimpleFluidMaterial("enriched_xenon_hexafluoronaquadate", 0x1e1ec2, "XeNq+F6");
    public static final Material AuricChloride = SimpleFluidMaterial("auric_chloride", 0xdffb50, "Au2Cl6");
    public static final Material BromineTrifluoride = SimpleFluidMaterial("bromine_trifluoride", 0xfcde1d, "BrF3");
    public static final Material XenoauricFluoroantimonicAcid = SimpleFluidMaterial("xenoauric_fluoroantimonic_acid", 0x685b08, "XeAuSbF6");
    public static final Material NaquadahSulfate = SimpleFluidMaterial("naquadah_sulfate", 0x38330f, "NqSO4");
    public static final Material NaquadahSolution = SimpleFluidMaterial("naquadah_solution", 0x523b3a, "NqNH4NO3");
    public static final Material ClearNaquadahLiquid = SimpleFluidMaterial("clear_naquadah_liquid", 0xa89f9e, "Nq?");
    public static final Material ComplicatedNaquadahGas = SimpleFluidMaterial("complicated_naquadah_gas", 0x403d3d, "Nq??");
    public static final Material ComplicatedHeavyNaquadah = SimpleFluidMaterial("complicated_heavy_naquadah", 0x403d3d, "Nq??");
    public static final Material ComplicatedMediumNaquadah = SimpleFluidMaterial("complicated_medium_naquadah", 0x403d3d, "Nq??");
    public static final Material ComplicatedLightNaquadah = SimpleFluidMaterial("complicated_light_naquadah", 0x403d3d, "Nq??");
    public static final Material NaquadahGas = SimpleFluidMaterial("naquadah_gas", 0x575757, "Nq");
    public static final Material LightNaquadah = SimpleFluidMaterial("light_naquadah", 0x2e2e2e, "Nq");
    public static final Material MediumNaquadah = SimpleFluidMaterial("medium_naquadah", 0x2e2e2e, "Nq");
    public static final Material HeavyNaquadah = SimpleFluidMaterial("heavy_naquadah", 0x2e2e2e, "Nq");
    public static final Material FCrackedLightNaquadah = SimpleFluidMaterial("fl_cracked_light_naquadah", 0x505e5b, "FlNq");
    public static final Material FCrackedMediumNaquadah = SimpleFluidMaterial("fl_cracked_medium_naquadah", 0x505e5b, "FlNq");
    public static final Material FCrackedHeavyNaquadah = SimpleFluidMaterial("fl_cracked_heavy_naquadah", 0x505e5b, "FlNq");
    public static final Material LightNaquadahFuel = SimpleFluidMaterial("light_naquadah_fuel", 0x2e2e2e, "Nq");
    public static final Material MediumNaquadahFuel = SimpleFluidMaterial("medium_naquadah_fuel", 0x2e2e2e, "Nq");
    public static final Material HeavyNaquadahFuel = SimpleFluidMaterial("heavy_naquadah_fuel", 0x2e2e2e, "Nq");
    public static final Material AmmoniumNitrate = SimpleFluidMaterial("ammonium_nitrate", Ammonia.getMaterialRGB(), "NH4NO3");
    public static final Material ENaquadahSolution = SimpleFluidMaterial("e_naquadah_solution", 0x523b3a, "Nq+?");
    public static final Material ClearENaquadahLiquid = SimpleFluidMaterial("clear_e_naquadah_liquid", 0xa89f9e, "Nq+?");
    public static final Material ComplicatedHeavyENaquadah = SimpleFluidMaterial("complicated_heavy_e_naquadah", 0x403d3d, "Nq+??");
    public static final Material ComplicatedMediumENaquadah = SimpleFluidMaterial("complicated_medium_e_naquadah", 0x403d3d, "Nq+??");
    public static final Material ComplicatedLightENaquadah = SimpleFluidMaterial("complicated_light_e_naquadah", 0x403d3d, "Nq+??");
    public static final Material LightENaquadah = SimpleFluidMaterial("light_e_naquadah", 0x2e2e2e, "Nq+");
    public static final Material MediumENaquadah = SimpleFluidMaterial("medium_e_naquadah", 0x2e2e2e, "Nq+");
    public static final Material HeavyENaquadah = SimpleFluidMaterial("heavy_e_naquadah", 0x2e2e2e, "Nq+");
    public static final Material RnCrackedLightNaquadah = SimpleFluidMaterial("rn_cracked_light_e_naquadah", 0x505e5b, "RnNq+");
    public static final Material RnCrackedMediumENaquadah = SimpleFluidMaterial("rn_cracked_medium_e_naquadah", 0x505e5b, "RnNq+");
    public static final Material RnCrackedHeavyENaquadah = SimpleFluidMaterial("rn_cracked_heavy_e_naquadah", 0x505e5b, "RnNq+");
    public static final Material LightENaquadahFuel = SimpleFluidMaterial("light_e_naquadah_fuel", 0x2e2e2e, "Nq+");
    public static final Material MediumENaquadahFuel = SimpleFluidMaterial("medium_e_naquadah_fuel", 0x2e2e2e, "Nq+");
    public static final Material HeavyENaquadahFuel = SimpleFluidMaterial("heavy_e_naquadah_fuel", 0x2e2e2e, "Nq+");
    public static final Material HyperFuelI = SimpleFluidMaterial("hyper_fluid_i", 0xfaff5e, "Nq(Nq+)(*Nq*)RfPu");
    public static final Material HyperFuelII = SimpleFluidMaterial("hyper_fluid_ii", 0xd8db67, "Nq(Nq+)(*Nq*)DbCm");
    public static final Material HyperFuelIII = SimpleFluidMaterial("hyper_fluid_iii", 0x8f9146, "Nq(Nq+)(*Nq*)AdCf");
    public static final Material HyperFuelIV = SimpleFluidMaterial("hyper_fluid_iv", 0x4d4e31, "Nq(Nq+)(*Nq*)AdCfNtTn");
    public static final Material AcidicSaltWater = SimpleFluidMaterial("acidic_salt_water", 0x006960, "H2SO4(NaCl)3(H2O)3Cl2");
    public static final Material SulfuricBromineSolution = SimpleFluidMaterial("sulfuric_bromine_solution", 0xff5100, "H2SO4Br(H2O)Cl2");
    public static final Material HotVapourMixture = SimpleFluidMaterial("hot_vapour_mixture", 0xff5100, "H2SO4Br(H2O)2Cl2");
    public static final Material DampBromine = SimpleFluidMaterial("damp_bromine", 0xe17594, "Br(H2O)");
    public static final Material Ethylhexanol = SimpleFluidMaterial("ethylhexanol", 0xfeea9a, "C8H18O");
    public static final Material DiethylhexylPhosphoricAcid = SimpleFluidMaterial("di_ethylhexyl_phosphoric_acid", 0xffff99, "C16H35O4P");
    public static final Material RareEarthHydroxidesSolution = SimpleFluidMaterial("rare_earth_hydroxides_solution", 0xcfb37d, "NaOH(H2O)?(OH)3");
    public static final Material RareEarthChloridesSolution = SimpleFluidMaterial("rare_earth_chlorides_solution", 0x164b45, "(?Cl3)H2O");
    public static final Material LaNdOxidesSolution = SimpleFluidMaterial("la_nd_oxides_solution", 0x9ce3db, "(La2O3)(Pr2O3)(Nd2O3)(Ce2O3)");
    public static final Material SmGdOxidesSolution = SimpleFluidMaterial("sm_gd_oxides_solution", 0xffff99, "(Sc2O3)(Eu2O3)(Gd2O3)(Sm2O3)");
    public static final Material TbHoOxidesSolution = SimpleFluidMaterial("tb_ho_oxides_solution", 0x99ff99, "(Y2O3)(Tb2O3)(Dy2O3)(Ho2O3)");
    public static final Material ErLuOxidesSolution = SimpleFluidMaterial("er_lu_oxides_solution", 0xffb3ff, "(Er2O3)(Tm2O3)(Yb2O3)(Lu2O3)");
    public static final Material SupercooledCryotheum = SimpleFluidMaterial("supercooled_cryotheum", Cryotheum.getMaterialRGB()-10, of(new MaterialStack(Cryotheum, 1)));
    public static final Material Turpentine = SimpleFluidMaterial("turpentine", 0x93bd46, "C10H16");
    public static final Material Acetylene = SimpleFluidMaterial("acetylene", 0x959c60, "C2H2");
    public static final Material Formaldehyde = SimpleFluidMaterial("formaldehyde", 0x95a13a, "CH2O");
    public static final Material PropargylAlcohol = SimpleFluidMaterial("propargyl_alcohol", 0xbfb32a, "CHCCH2OH");
    public static final Material PropargylChloride = SimpleFluidMaterial("propargyl_chloride", 0x918924, "HC2CH2Cl");
    public static final Material Citral = SimpleFluidMaterial("citral", 0xf2e541, "C10H16O");
    public static final Material BetaIonone = SimpleFluidMaterial("beta_ionone", 0xdc5ce6, "C13H20O");
    public static final Material VitaminA = SimpleFluidMaterial("vitamin_a", 0x8d5c91, "C20H30O");
    public static final Material EthyleneOxide = SimpleFluidMaterial("ethylene_oxide", 0xa0c3de, "C2H4O");
    public static final Material Ethanolamine = SimpleFluidMaterial("ethanolamine", 0x6f7d87, "HOCH2CH2NH2");
    public static final Material Biotin = SimpleFluidMaterial("biotin", 0x68cc6a, "C10H16N2O3S");
    public static final Material B27Supplement = SimpleFluidMaterial("b_27_supplement", 0x386939, "C142H230N36O44S");
    public static final Material CleanAmmoniaSolution = SimpleFluidMaterial("clear_ammonia_solution", 0x53c9a0, "NH3(H2O)");
    public static final Material Catalase = SimpleFluidMaterial("catalase", 0xdb6596, "?");
    public static final Material Blood = SimpleFluidMaterial("blood", 0x5c0606, "Blood");
    public static final Material BloodCells = SimpleFluidMaterial("blood_cells", 0xad2424, "???");
    public static final Material BloodPlasma = SimpleFluidMaterial("blood_plasma", 0xe37171, "???");
    public static final Material BFGF = SimpleFluidMaterial("bfgf", 0xb365e0, "bFGF");
    public static final Material EGF = SimpleFluidMaterial("egf", 0x815799, "C257H381N73O83S7");
    public static final Material NitroBenzene = SimpleFluidMaterial("nitro_benzene", 0x81c951, "C6H5NO2");
    public static final Material Aniline = SimpleFluidMaterial("aniline", 0x4c911d, "C6H5NH2");
    public static final Material ChlorosulfonicAcid = SimpleFluidMaterial("chlorosulfonic_acid", 0x916c1d, "HSO3Cl");
    public static final Material Sulfanilamide = SimpleFluidMaterial("sulfanilamide", 0x523b0a, "C6H8N2O2S");
    public static final Material SilicaGelBase = SimpleFluidMaterial("silica_gel_base", 0x27a176, "SiO2(HCl)(NaOH)(H2O)");
    public static final Material Ethanol100 = SimpleFluidMaterial("ethanol_100", Ethanol.getMaterialRGB(), "C2H5OH");
    public static final Material PiranhaSolution = SimpleFluidMaterial("piranha_solution", 0x4820ab, "(H2SO4)H2O2");
    public static final Material WaterAgarMix = SimpleFluidMaterial("water_agar_mix", 0x48dbbe, "H2O?");
    public static final Material BacterialGrowthMedium = SimpleFluidMaterial("bacterial_growth_medium", 0x0b2e12, "For Bacteria");
    public static final Material DepletedGrowthMedium = SimpleFluidMaterial("depleted_growth_medium", 0x071209, "Depleted");
    public static final Material AnimalCells = SimpleFluidMaterial("animal_cells", 0xc94996, "???");
    public static final Material RapidlyReplicatingAnimalCells = SimpleFluidMaterial("rapidly_replicating_animal_cells", 0x7a335e, "????");
    public static final Material MycGene = SimpleFluidMaterial("myc_gene", 0x445724, "?");
    public static final Material Oct4Gene = SimpleFluidMaterial("oct_4_gene", 0x374f0d, "?");
    public static final Material SOX2Gene = SimpleFluidMaterial("sox_2_gene", 0x5d8714, "?");
    public static final Material KFL4Gene = SimpleFluidMaterial("kfl_4_gene", 0x759143, "?");
    public static final Material Cas9 = SimpleFluidMaterial("cas_9", 0x5f6e46, "?");
    public static final Material GenePlasmids = SimpleFluidMaterial("pluripotency_induction_gene_plasmids", 0xabe053, "?");
    public static final Material Chitin = SimpleFluidMaterial("chitin", 0xcbd479, "?");
    public static final Material Chitosan = SimpleFluidMaterial("chitosan", 0xb1bd42, "?");
    public static final Material GeneTherapyFluid = SimpleFluidMaterial("pluripotency_induction_gene_therapy_fluid", 0x6b2f66, "?");
    public static final Material Resin = SimpleFluidMaterial("resin", 0x3d2f11, "?");
    public static final Material LinoleicAcid = SimpleFluidMaterial("linoleic_acid", 0xD5D257, "C18H32O2");
    public static final Material SiliconFluoride = SimpleFluidMaterial("silicon_fluoride", 0xB2B4B4, "SiF4");
    public static final Material CarbonFluoride = SimpleFluidMaterial("carbone_fluoride", 0xE6E6E6, "CF4");
    public static final Material PhosphorusTrichloride = SimpleFluidMaterial("phosphorus_trichloride", (Phosphorus.getMaterialRGB()+Chlorine.getMaterialRGB())/2, "PCl3");
    public static final Material PhosphorylChloride = SimpleFluidMaterial("phosphoryl_chloride", 0xE6E6E6, "POCl3");
    public static final Material TributylPhosphate = SimpleFluidMaterial("tributyl_phosphate", 0x7C5B2C, "(C4H9)3PO4");
    public static final Material Butanol = SimpleFluidMaterial("butanol", (FermentedBiomass.getMaterialRGB()+20), "C4H9OH");
    public static final Material RedOil = SimpleFluidMaterial("red_oil", 0x7C1500, "H2N4(RP-1)NiZnFe4");
    public static final Material HydrogenCyanide = SimpleFluidMaterial("hydrogen_cyanide", 0xb6d1ae, "HCN");
    public static final Material SodiumCyanide = SimpleFluidMaterial("sodium_cyanide", 0x5f7c8c, "NaCN");
    public static final Material GoldCyanide = SimpleFluidMaterial("gold_cyanide", 0x8c8761, "AuCN");
    public static final Material ChlorideLeachedSolution = SimpleFluidMaterial("chloride_leached_solution", 0x41472e, "CaCl2(CuCl2)(PbCl2)(BiCl3)(FeCl2)");
    public static final Material MolybdenumFlue = SimpleFluidMaterial("molybdenum_flue_gas", 0x333338, "H2OReS?");
    public static final Material RheniumSulfuricSolution = SimpleFluidMaterial("rhenium_sulfuric_solution", 0xbabaff, "ReS?");
    public static final Material AmmoniumSulfate = SimpleFluidMaterial("ammonium_sulfate", 0x6464f5, /*AMMONIUM.formulaGroup(2)+SULFATE.formula()*/ "(NH4)2SO4");
    public static final Material AmmoniumPerrhenate = SimpleFluidMaterial("ammonium_perrhenate", 0x1c1c45, "NH4"+"Re"+"O4");
    public static final Material ElectronDegenerateRheniumPlasma = SimpleFluidMaterial("degenerate_rhenium_plasma", 0x6666FF, "Rh", false, true);
    public static final Material BoricAcid = SimpleFluidMaterial("boric_acid", 0xD5D2D7, "H3BO3");
    public static final Material FluoroBoricAcid = SimpleFluidMaterial("fluoroboric_acid", 0xD5D2D7, "HBF4");
    public static final Material BenzenediazoniumTetrafluoroborate = SimpleFluidMaterial("benzenediazonium_tetrafluoroborate", 0xD5D2D7, "C6H5BF4N2");
    public static final Material BoronFluoride = SimpleFluidMaterial("boron_fluoride", 0xD5D2D7, "BF3");
    public static final Material FluoroBenzene = SimpleFluidMaterial("fluoro_benzene", 0xD5D2D7, "C6H5F");
    public static final Material SodiumNitrateSolution = SimpleFluidMaterial("sodium_nitrate_solution", 0xA09ED7, "(H2O)"+"NaNO3");
    public static final Material Fluorotoluene = SimpleFluidMaterial("fluorotoluene", 0xE0DA99, "C7H7F");
    public static final Material OrthoXylene = SimpleFluidMaterial("ortho_xylene", 0xB9575E, "C6H4(CH3)2");
    public static final Material OrthoXyleneZeoliteMixture = SimpleFluidMaterial("ortho_xylene_zeolite", 0xB9785E, "(NaC4Si27Al9(H2O)28O72)C6H4(CH3)2");
    public static final Material ParaXylene = SimpleFluidMaterial("para_xylene", 0xB9575E, "C6H4(CH3)2");
    public static final Material Dibromomethylbenzene = SimpleFluidMaterial("dibromomethylbenzene", 0x0A1D2C, "C7H6Br2");
    public static final Material AceticAnhydride = SimpleFluidMaterial("acetic_anhydride", 0xD5DDDF, "(CH3CO)2O");
    public static final Material Isochloropropane = SimpleFluidMaterial("isochloropropane", 0xD5DD95, "CH3CClCH3");
    public static final Material Resorcinol = SimpleFluidMaterial("resorcinol", 0xD5DDBE, "C6H6O2");
    public static final Material Dinitrodipropanyloxybenzene = SimpleFluidMaterial("dinitrodipropanyloxybenzene", 0x83945F, "C12H16O2(NO2)2");
    public static final Material Naphthaldehyde = SimpleFluidMaterial("napthaldehyde", 0xBCA853, "C10H7CHO");
    public static final Material HydrobromicAcid = SimpleFluidMaterial("hydrobromic_acid", 0xBC6C53, "HBr");
    public static final Material ThionylChloride = SimpleFluidMaterial("thionyl_chloride", 0xF9F7E5, "SOCl2");
    public static final Material Diisopropylcarbodiimide = SimpleFluidMaterial("diisopropylcarbodiimide", 0xA0CFFE, "C7H14N2");
    public static final Material Pyridine = SimpleFluidMaterial("pyridine", (Ammonia.getMaterialRGB()+Formaldehyde.getMaterialRGB())/2, "C5H5N");
    public static final Material Phenylpentanoicacid = SimpleFluidMaterial("phenylpentanoicacid", (Butene.getMaterialRGB()+CarbonMonoxide.getMaterialRGB())/2, "C11H14O2");
    public static final Material Dimethylsulfide = SimpleFluidMaterial("dimethylsulfide", (Methanol.getMaterialRGB()+HydrogenSulfide.getMaterialRGB())/2, "(CH3)2S");
    public static final Material BenzoylChloride = SimpleFluidMaterial("benzoyl_chloride", (Toluene.getMaterialRGB()+ThionylChloride.getMaterialRGB())/2, "C7H5ClO");
    public static final Material Silvertetrafluoroborate = SimpleFluidMaterial("silvertetrafluoroborate", (SilverOxide.getMaterialRGB()+BoronFluoride.getMaterialRGB())/2, "AgBF4");
    public static final Material PCBA = SimpleFluidMaterial("pcba", (Chlorobenzene.getMaterialRGB()+Dimethylsulfide.getMaterialRGB()+Phenylpentanoicacid.getMaterialRGB())/3, "C72H14O2");
    public static final Material PCBS = SimpleFluidMaterial("pcbs", (Styrene.getMaterialRGB()+PCBA.getMaterialRGB()-40)/2, "C80H21O2");
    public static final Material Ferrocene = SimpleFluidMaterial("ferrocene", (Water.getMaterialRGB()+Chlorine.getMaterialRGB()+Iron.getMaterialRGB())/3, "C10H10Fe");
    public static final Material Ferrocenylfulleropyrrolidine = SimpleFluidMaterial("ferrocenylfulleropyrddolidine", (Ferrocene.getMaterialRGB()+Ethylene.getMaterialRGB()+ CarbonMonoxide.getMaterialRGB())/3, "C74H19FeN");
    public static final Material Hydroquinone = SimpleFluidMaterial("hydroquinone", (Oxygen.getMaterialRGB()+Propene.getMaterialRGB())/2, "C6H4(OH)2");
    public static final Material SodiumAcetate = SimpleFluidMaterial("sodium_acetate", (Sodium.getMaterialRGB()+AceticAnhydride.getMaterialRGB())/2, "C2H3NaO2");
    public static final Material PotassiumHydroxide = SimpleFluidMaterial("potassium_hydroxide", (Potassium.getMaterialRGB()+Hydrogen.getMaterialRGB()+Oxygen.getMaterialRGB())/3, "KOH");
    public static final Material Methylamine = SimpleFluidMaterial("methylamine", (Methanol.getMaterialRGB()+Ammonia.getMaterialRGB())/2, "CH3NH2");
    public static final Material Phosgene = SimpleFluidMaterial("phosgene", (Chlorine.getMaterialRGB()+CarbonMonoxide.getMaterialRGB())/2, "COCl2");
    public static final Material IsopropylAlcohol = SimpleFluidMaterial("isopropyl_alcohol", (Water.getMaterialRGB()+Propene.getMaterialRGB())/2, "C3H8O");
    public static final Material VanadiumWasteSolution = SimpleFluidMaterial("vanadium_waste_solution", 0xbf95f5, "NaCl(Na2SO4)(SiO2)(Al(OH)3)");
    public static final Material UranylChlorideSolution = SimpleFluidMaterial("uranyl_chloride_solution", 0xdfe018, "UO2Cl2(H2O)?");
    public static final Material UranylNitrateSolution = SimpleFluidMaterial("uranyl_nitrate_solution", 0xdfe018, "UO2(NO3)2(H2O)?]");
    public static final Material UraniumSulfateWasteSolution = SimpleFluidMaterial("uranium_sulfate_waste_solution", 0xdfe018, "PbRaSr(H2SO4)");
    public static final Material PurifiedUranylNitrate = SimpleFluidMaterial("purified_uranyl_nitrate_solution", 0xeff028, "UO2(NO3)2(H2O)");
    public static final Material UraniumDiuranate = SimpleFluidMaterial("uranium_diuranate", 0xeff028, "(NH4)2U2O7");
    public static final Material UraniumRefinementWasteSolution = SimpleFluidMaterial("uranium_refinement_waste_solution", 0xeff028, "H2SO4C?");
    public static final Material ThoriumNitrateSolution = SimpleFluidMaterial("thorium_nitrate_solution", 0x33bd45, "Th(NO3)4(H2O)");
    public static final Material SodiumHexafluoroaluminate = SimpleFluidMaterial("sodium_hexafluoroaluminate", (Sodium.getMaterialRGB()+Aluminium.getMaterialRGB()+Fluorine.getMaterialRGB())/3, "Na3AlF6");
    public static final Material SodiumCarbonateSolution = SimpleFluidMaterial("sodium_carbonate_solution", (SodaAsh.getMaterialRGB()+30), "Na2CO3(H2O)");
    public static final Material SodiumSulfateSolution = SimpleFluidMaterial("sodium_sulfate_solution", (SodiumSulfate.getMaterialRGB()+30), "Na2SO4(H2O) ");
    public static final Material SodiumChromateSolution = SimpleFluidMaterial("sodium_chromate_solution", 0xf2e70f, "Na2CrO4(H2O)");
    public static final Material SodiumDichromateSolution = SimpleFluidMaterial("sodium_dichromate_solution", 0xf2750f, "Na2Cr2O7");
    public static final Material RichNitrogenMix = SimpleFluidMaterial("rich_nitrogen_mix", 0x6891d8, "H2O(CH4)?");
    public static final Material OxidisedNitrogenMix = SimpleFluidMaterial("oxidised_nitrogen_mix", 0x708ACD, "(H2O)2(CH4)??");
    public static final Material PurifiedNitrogenMix = SimpleFluidMaterial("purified_nitrogen_mix", 0x6891d8, "(H2O)2(CH4)?");
    public static final Material CarbonatedEthanolamine = SimpleFluidMaterial("carbonated_ethanolamine", 0x6f7d87, "H2NCH2CH2OHC");
    public static final Material AmmoniaRichMix = SimpleFluidMaterial("ammonia_rich_mix", 0x2f5d99, "NH3((H2O)2(CH4)?)");
    public static final Material DissolvedLithiumOre = SimpleFluidMaterial("dissolved_lithium_ores", 0x664850, "LiAlO2(H2SO4)");
    public static final Material LithiumCarbonateSolution = SimpleFluidMaterial("lithium_carbonate_solution", (Lithium.getMaterialRGB()+Carbon.getMaterialRGB()+Oxygen.getMaterialRGB())/3, "Li2CO3(H2O)");
    public static final Material LithiumChlorideSolution = SimpleFluidMaterial("lithium_chloride_solution", (Lithium.getMaterialRGB()+Chlorine.getMaterialRGB()), "LiCl(H2O)");
    public static final Material CalicheIodateBrine = SimpleFluidMaterial("caliche_iodate_brine", 0xffe6660, "(H2O)NaNO3KNO3KClNaIO3");
    public static final Material IodideSolution = SimpleFluidMaterial("iodide_solution", 0x08081c, "(H2O)NaNO3KNO3KClNaI");
    public static final Material CalicheNitrateSolution = SimpleFluidMaterial("caliche_nitrate_solution", 0xffe6660, "(H2O)NaNO3KNO3KClNaOH");
    public static final Material CalicheIodineBrine = SimpleFluidMaterial("caliche_iodine_brine", 0xffe6660, "(H2O)NaNO3KNO3KClNaOHI");
    public static final Material KeroseneIodineSolution = SimpleFluidMaterial("kerosene_iodine_solution", 0x08081c, "C12H26I");
    public static final Material IodizedBrine = SimpleFluidMaterial("iodized_brine", 0x525242, "I?");
    public static final Material IodineBrineMix = SimpleFluidMaterial("iodine_brine_mix", 0x525242, "I??");
    public static final Material IodineSlurry = SimpleFluidMaterial("iodine_slurry", 0x08081c, "I?");
    public static final Material Brine = SimpleFluidMaterial("brine", 0xfcfc8a, "?");
    public static final Material MesitylOxide = SimpleFluidMaterial("mesityl_oxide", Acetone.getMaterialRGB()-10, "C6H10O");
    public static final Material MethylIsobutylKetone = SimpleFluidMaterial("methyl_isobutyl_ketone", (MesitylOxide.getMaterialRGB()+WaterAgarMix.getMaterialRGB())/2, "C6H12O");
    public static final Material ThiocyanicAcid = SimpleFluidMaterial("thiocyanic_acid", 0xfcfc30, "HSCN");
    public static final Material ZrHfSeparationMix = SimpleFluidMaterial("zrhf_separation_mix", 0xfcfc95, "?");
    public static final Material ZrHfChloride = SimpleFluidMaterial("zrhf_chloride", 0x51d351, "ZrHfCl4");
    public static final Material ZrHfOxyChloride = SimpleFluidMaterial("zrhf_oxychloride", 0x51d351, "Cl2HfOZr");
    public static final Material ZirconChlorinatingResidue = SimpleFluidMaterial("zircon_chlorinating_residue", 0x51d351, "(SiCl4)Co?");
    public static final Material ZincExhaustMixture = SimpleFluidMaterial("zinc_exhaust_mixture", (CarbonDioxide.getMaterialRGB()+SulfurDioxide.getMaterialRGB())/2, "(SO2)(CO2)?");
    public static final Material ZincSlagSlurry = SimpleFluidMaterial("zinc_slag_slurry", (Zinc.getMaterialRGB()-20), "H2O?");
    public static final Material MetalRichSlagSlurry = SimpleFluidMaterial("metal_slag_slurry", (Zinc.getMaterialRGB()-10), "?");
    public static final Material AcidicMetalSlurry = SimpleFluidMaterial("acidic_metal_slurry", (Zinc.getMaterialRGB()-10+PhosphoricAcid.getMaterialRGB())/2, "H3PO4?");
    public static final Material SeparatedMetalSlurry = SimpleFluidMaterial("separated_metal_slurry", (Zinc.getMaterialRGB()-20), "H3PO4?");
    public static final Material MetalHydroxideMix = SimpleFluidMaterial("metal_hydroxide_mix", (Zinc.getMaterialRGB()-30), "?ZnOH");
    public static final Material ZincPoorMix = SimpleFluidMaterial("zinc_poor_mix", (Iron.getMaterialRGB()-10), "?Fe");
    public static final Material IronPoorMix = SimpleFluidMaterial("iron_poor_mix", (Copper.getMaterialRGB()+10), "?In");
    public static final Material IndiumHydroxideConcentrate = SimpleFluidMaterial("indium_hydroxide_concentrate", (Indium.getMaterialRGB()+Hydrogen.getMaterialRGB()+10)/2, "In(OH)3");
    public static final Material CadmiumThalliumLiquor = SimpleFluidMaterial("cdtl_liquor", (Cadmium.getMaterialRGB()+Thallium.getMaterialRGB()+RareEarth.getMaterialRGB())/3, "(H2SO4)CdTl");
    public static final Material ZincAmalgam = SimpleFluidMaterial("zinc_amalgam", (Zinc.getMaterialRGB()-20), "ZnHg");
    public static final Material CadmiumSulfateSolution = SimpleFluidMaterial("cadmium_sulfate", (Cadmium.getMaterialRGB()+SulfuricAcid.getMaterialRGB())/2, "CdSO4?");
    public static final Material ThalliumSulfateSolution = SimpleFluidMaterial("thallium_sulfate", (Thallium.getMaterialRGB()+SulfuricAcid.getMaterialRGB())/2, "Tl2SO4?");
    public static final Material PolyphenolMix = SimpleFluidMaterial("polyphenol_mix", (Phenol.getMaterialRGB()+10), "?");
    public static final Material AcidifiedPolyphenolMix = SimpleFluidMaterial("acidified_polyphenol_mix", (PolyphenolMix.getMaterialRGB()+SulfuricAcid.getMaterialRGB())/2, "?");
    public static final Material Diethylether = SimpleFluidMaterial("diethylether", AcidifiedPolyphenolMix.getMaterialRGB()-20, "(C2H5)2O");
    public static final Material TannicAcid = SimpleFluidMaterial("tannic_acid", (Diethylether.getMaterialRGB()+AcidifiedPolyphenolMix.getMaterialRGB())/4, "C76H52O46");
    public static final Material GermanicAcidSolution = SimpleFluidMaterial("germanic_acid_solution", (Germanium.getMaterialRGB()-10), "H4GeO4");
    public static final Material GermaniumChloride = SimpleFluidMaterial("germanium_chloride", (Germanium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, "GeCl4");
    public static final Material SodiumHydroxideSolution = SimpleFluidMaterial("sodium_hydroxide_solution", SodiumHydroxide.getMaterialRGB()+50, "(H2O)NaOH");
    public static final Material LithiumHydroxideSolution = SimpleFluidMaterial("lithium_hydroxide_solution", (Lithium.getMaterialRGB()+Oxygen.getMaterialRGB()+Hydrogen.getMaterialRGB())/3, "(H2O)LiOH");
    public static final Material LithiumPeroxideSolution = SimpleFluidMaterial("lithium_peroxide", (Lithium.getMaterialRGB()+Oxygen.getMaterialRGB())/2, "(H2O)Li2O2");
    public static final Material Ozone = SimpleFluidMaterial("ozone", 0x0099FF, "O3");
    public static final Material NitrogenPentoxide = SimpleFluidMaterial("nitrogen_pentoxide", 0x0033C0, "N2O5");
    public static final Material AcryloNitrile = SimpleFluidMaterial("acrylonitrile", 0x9999ff, "CH2CHCN");
    public static final Material SodiumThiocyanate = SimpleFluidMaterial("sodium_thiocyanate", (Sodium.getMaterialRGB()+Sulfur.getMaterialRGB())/2, "NaSCN");
    public static final Material PolyacrylonitrileSolution = SimpleFluidMaterial("polyacrylonitrile_solution", 0x9999ff, "(C3H3N)n(NaSCN)");
    public static final Material MethylFormate = SimpleFluidMaterial("methyl_formate", 0Xff9999, "HCOOCH3");
    public static final Material WetFormamide = SimpleFluidMaterial("wet_formamide", 0x33CCFF, "(H2O)CH3NO");
    public static final Material Formamide = SimpleFluidMaterial("formamide", 0x33CCFF, "CH3NO");
    public static final Material HydroxylamineDisulfate = SimpleFluidMaterial("hydroxylamine_disulfate", 0x99add6, "(NH3OH)2(NH4)2(SO4)2");
    public static final Material Hydroxylamine = SimpleFluidMaterial("hydroxylamine", 0x99cc99, "H3NO");
    public static final Material Amidoxime = SimpleFluidMaterial("amidoxime", 0x66ff33, "H3N2O(CH)");
    public static final Material PureUranylNitrateSolution = SimpleFluidMaterial("pure_uranyl_nitrate", 0x33bd45, "(H2O)UO2(NO3)2");
    public static final Material CarbonSulfide = SimpleFluidMaterial("carbon_sulfide", 0x40ffbf, "CS2");
    public static final Material AmineMixture = SimpleFluidMaterial("amine_mixture", (Methanol.getMaterialRGB()-20+Ammonia.getMaterialRGB()-10)/2, "(NH3)CH4");
    public static final Material DimethylthiocarbamoilChloride = SimpleFluidMaterial("dimethylthiocarbamoil_chloride", 0xd9ff26, "(CH3)2NC(S)Cl");
    public static final Material Trimethylamine = SimpleFluidMaterial("trimetylamine", (Dimethylamine.getMaterialRGB()+20), "(CH3)3N");
    public static final Material Mercaptophenol = SimpleFluidMaterial("mercaptophenol", 0xbaaf18, "C6H6OS");
    public static final Material Dimethylformamide = SimpleFluidMaterial("dimethylformamide", 0x42bdff, "(CH3)2NCH");
    public static final Material Oct1ene = SimpleFluidMaterial("1_octene", 0x7e8778, "C8H16");
    public static final Material CetaneTrimethylAmmoniumBromide = SimpleFluidMaterial("cetane_trimethyl_ammonium_bromide", 0xb9c1c9, "C19H42BrN");
    public static final Material AmmoniumPersulfate = SimpleFluidMaterial("ammonium_persulfate", 0x6464f5, "(NH4)2S2O8");
    public static final Material DebrominatedWater = SimpleFluidMaterial("debrominated_brine", 0x0000ff, "H20");
    public static final Material SeaWater = SimpleFluidMaterial("sea_water", 0x0000FF, "H2O"+"?");
    public static final Material ConcentratedBrine = SimpleFluidMaterial("concentrated_brine", 0xfcfc95, "?");
    public static final Material CalciumFreeBrine = SimpleFluidMaterial("calcium_free_brine", 0xfcfca6, "?");
    public static final Material SodiumFreeBrine = SimpleFluidMaterial("sodium_free_brine", 0xfcfcb1, "?");
    public static final Material PotassiumFreeBrine = SimpleFluidMaterial("potassium_free_brine", 0xfcfcbc, "?");
    public static final Material BoronFreeSolution = SimpleFluidMaterial("boron_free_solution", 0xfcfccd, "?");
    public static final Material SodiumLithiumSolution = SimpleFluidMaterial("sodium_lithium_solution", 0xfcfccd, "NaLi?");
    public static final Material ChilledBrine = SimpleFluidMaterial("chilled_brine", 0xfcfc95, "?");
    public static final Material MagnesiumContainingBrine = SimpleFluidMaterial("magnesium_containing_brine", 0xfcfcbc, "Mg?");
    public static final Material BrominatedBrine = SimpleFluidMaterial("brominated_brine", 0xfdd48d, "Br?");
    public static final Material AcidicBrominatedBrine = SimpleFluidMaterial("acidic_brominated_brine", 0xfdd48d, "(H2SO4)Cl?");
    public static final Material ButylLithium = SimpleFluidMaterial("butyl_lithium", (Butane.getMaterialRGB()+Lithium.getMaterialRGB())/2, "C4H9Li");
    public static final Material Acetaldehyde = SimpleFluidMaterial("acetaldehyde", 0xFF9933, "C2H4O");
    public static final Material Benzaldehyde = SimpleFluidMaterial("benzaldehyde", 0xb26f22, "C7H6O");
    public static final Material Dibenzylideneacetone = SimpleFluidMaterial("dibenzylideneacetone", 0Xcc6699, "C17H14O");
    public static final Material TrimethyltinChloride = SimpleFluidMaterial("trimethyltin_chloride", 0x8c8075, "(CH3)3SnCl");
    public static final Material ChloroPlatinicAcid = SimpleFluidMaterial("chloroplatinic_acid", 0xffba54, "H2PtCl6");
    public static final Material Cyclooctadiene = SimpleFluidMaterial("cyclooctadiene", 0x33CC33, "C8H12");
    public static final Material Cycloparaphenylene = SimpleFluidMaterial("cycloparaphenylene", 0x333333, "CPP");
    public static final Material SuperheavyMix = SimpleFluidMaterial("superheavy_mix", 0x403737, "SgBhRfDb");
    public static final Material NeutronPlasma = SimpleFluidMaterial("neutron_plasma", 0xf0e9e9, "n");
    public static final Material HotMetastableOganesson = SimpleFluidMaterial("hot_oganesson", 0x521973, "Og");
    public static final Material TitaniumTetrafluoride = SimpleFluidMaterial("titanium_tetrafluoride", Titanium.getMaterialRGB());
    public static final Material Titanium50Tetrafluoride = SimpleFluidMaterial("titanium_50_tetrafluoride", Titanium.getMaterialRGB());
    public static final Material Carbon12 = SimpleFluidMaterial("carbon_12", Carbon.getMaterialRGB(), "C_12");
    public static final Material Carbon13 = SimpleFluidMaterial("carbon_13", Carbon.getMaterialRGB(), "C_13");
    public static final Material Nitrogen14 = SimpleFluidMaterial("nitrogen_14", Nitrogen.getMaterialRGB(), "N_14");
    public static final Material NItrogen15 = SimpleFluidMaterial("nitrogen_15", Nitrogen.getMaterialRGB(), "N_15");
    public static final Material CNOcatalyst = SimpleFluidMaterial("cno_catalyst", (Nitrogen.getMaterialRGB() + Carbon.getMaterialRGB()) / 2, "(C_12)(C_13)(N_14)(N_15)");
    public static final Material Calcium44 = SimpleFluidMaterial("calcium_44", Calcium.getMaterialRGB(), "Ca_44");
    public static final Material OgannesonBreedingBase = SimpleFluidMaterial("og_breeding_base", ((Titanium.getMaterialRGB() + 0xA85A12) / 2), "(Ti_50)Cf_252");
    public static final Material QuassifissioningPlasma = SimpleFluidMaterial("quasifissioning_plasma", 0xD5CB54, "???");
    public static final Material Ytterbium178 = SimpleFluidMaterial("ytterbium_178", Ytterbium.getMaterialRGB(), "Yb_178");
    public static final Material FlYbPlasma = SimpleFluidMaterial("flyb_plasma", (Ytterbium.getMaterialRGB() + 0x521973) / 2, "FlYb");
    public static final Material Chromium48 = SimpleFluidMaterial("chromium_48", ChromiumTrioxide.getMaterialRGB(), true, "Cr_48");
    public static final Material Iron52 = SimpleFluidMaterial("iron_52", Iron.getMaterialRGB(), true, "Fe_52");
    public static final Material Nickel56 = SimpleFluidMaterial("nickel_56", Nickel.getMaterialRGB(), true, "Ni_56");
    public static final Material Titanium44 = SimpleFluidMaterial("titanium_44", Titanium.getMaterialRGB(), true, "Ti_44");
    public static final Material HeliumCNO = SimpleFluidMaterial("helium_rich_cno", 0x59ffa6, true, "He?");
    //public static final Material PlasmaChromium48 = SimpleFluidMaterial("chromium48_plasma", Chrome.getMaterialRGB());
    //public static final Material PlasmaIron52 = SimpleFluidMaterial("iron52_plasma", Iron.getMaterialRGB());
    //public static final Material PlasmaNickel56 = SimpleFluidMaterial("nickel56_plasma", Nickel.getMaterialRGB());
    //public static final Material PlasmaTitanium44 = SimpleFluidMaterial("titanium44_plasma", Titanium.getMaterialRGB());
    //public static final Material PlasmaHeliumCNO = SimpleFluidMaterial("helium_rich_cno_plasma", 0x59ffa6);
    public static final Material SeleniteTelluriteMix = SimpleFluidMaterial("selenite_tellurite_mixture", 0x765A30, "TeO2SeO2(Na2CO3)2");
    public static final Material SeleniteSolution = SimpleFluidMaterial("selenite_solution", 0xc1c46a, "Na2SeO3");
    public static final Material CopperRefiningSolution = SimpleFluidMaterial("copper_refining_solution", 0x765A30, "CuH2SO4");
    public static final Material SodiumHydroxideBauxite = SimpleFluidMaterial("sodium_hydroxide_bauxite", 0xbf731a, "Al2H2O4");
    public static final Material ImpureAluminiumHydroxideSolution = SimpleFluidMaterial("impure_aloh_3_soution", 0xd8653e, "(H2O)Al(OH)3?");
    public static final Material PureAluminiumHydroxideSolution = SimpleFluidMaterial("pure_aloh_3_soution", (Aluminium.getMaterialRGB()+Oxygen.getMaterialRGB()+ Hydrogen.getMaterialRGB()+40)/2, "(H2O)Al2(OH)6");
    public static final Material RedMud = SimpleFluidMaterial("red_mud", 0xcc3300, "HCl?");
    public static final Material NeutralisedRedMud = SimpleFluidMaterial("neutralised_red_mud", 0xcc3300, "Fe??");
    public static final Material FerricREEChloride = SimpleFluidMaterial("ferric_ree_chloride", 0x30301a, "Fe?");
    public static final Material RedSlurry = SimpleFluidMaterial("red_slurry", 0xcc3300, "TiO2?");
    public static final Material TitanylSulfate = SimpleFluidMaterial("titanyl_sulfate", 0xdc3d7c, "TiO(SO4)");
    public static final Material DiluteNitricAcid = SimpleFluidMaterial("dilute_nitric_acid", (NitricAcid.getMaterialRGB() + Water.getMaterialRGB()) / 2, "(H2O)HNO3");
    public static final Material NbTaSeparationMixture = SimpleFluidMaterial("nbta_separation_mixture", 0xbcac93, "C18H39O5P");
    public static final Material FluoroniobicAcid = SimpleFluidMaterial("fluroniobic_acid", 0x73ff00, "NbHF7");
    public static final Material FluorotantalicAcid = SimpleFluidMaterial("flurotantalic_acid", 0x73ff00, "TaHF7");
    public static final Material NbTaFluorideMix = SimpleFluidMaterial("nbta_fluoride_mix", 0xbcac93, "(H2NbOF5)(H2TaF7)");
    public static final Material OxypentafluoroNiobate = SimpleFluidMaterial("oxypentafluoroniobate", 0x73ff00, "H2NbOF5");
    public static final Material HeptafluoroTantalate = SimpleFluidMaterial("heptafluorotantalate", 0x73ff00, "H2TaF7");
    public static final Material REEThUSulfateSolution = SimpleFluidMaterial("reethu_sulfate_solution", 0x89be5c, "?SO4");
    public static final Material RareEarthNitrateSolution = SimpleFluidMaterial("rare_earth_nitrate_solution", 0xcfb37d, "?NO3");
    public static final Material AlkalineEarthSulfateSolution = SimpleFluidMaterial("alkalineearth_sulfate", 0xe6ebff, "?SO4");
    public static final Material WetEthyleneOxide = SimpleFluidMaterial("wet_etylene_oxide", 0x90b3ff, "(H2O)C2H4O");
    public static final Material EthyleneGlycol = SimpleFluidMaterial("ethylene_glycol", 0x8080fa, "C2H6O2");
    public static final Material Chloroethanol = SimpleFluidMaterial("chloroethanol", 0xcfb050, "C2H5ClO");
    public static final Material Choline = SimpleFluidMaterial("choline", 0x63e45f, "C5H14NO");
    public static final Material ATL = SimpleFluidMaterial("atl", 0x709c4a, "ATL");
    public static final Material HotNitrogen = SimpleFluidMaterial("hot_nitrogen", Nitrogen.getMaterialRGB(), "N");
    public static final Material ViscoelasticPolyurethane = SimpleFluidMaterial("viscoelastic_polyurethane", 0xeffcef, "C17H16N2O4?");
    public static final Material ViscoelasticPolyurethaneFoam = SimpleFluidMaterial("viscoelastic_polyurethane_foam", 0xeffcef, "C17H16N2O4?");
    public static final Material CalciumCarbonateSolution = SimpleFluidMaterial("calcium_carbonate_solution", Calcite.getMaterialRGB(), "(H2O)CaCO3");
    public static final Material BariumSulfateSolution = SimpleFluidMaterial("barium_sulfate_solution", Barite.getMaterialRGB(), "(H2O)BaSO4");
    public static final Material BentoniteClaySlurry = SimpleFluidMaterial("bentonite_clay_solution", 0xdbc9c5, "H2O?");
    public static final Material DrillingMud = SimpleFluidMaterial("drilling_mud", 0x996600, "For the Void Miner");
    public static final Material UsedDrillingMud = SimpleFluidMaterial("used_drilling_mud", 0x998833, "Used Mud");
    public static final Material TolueneDiisocyanate = SimpleFluidMaterial("toluene_diisocyanate", 0xbaf6ca, "C9H6N2O2");
    public static final Material HydroselenicAcid = SimpleFluidMaterial("hydroselenic_acid", Selenium.getMaterialRGB(), "H2Se");
    public static final Material Aminophenol = SimpleFluidMaterial("aminophenol", 0xafca3a, "C6H4(OH)(NH2)");
    public static final Material Hydroxyquinoline = SimpleFluidMaterial("hydroxyquinoline", 0x3a9a71, "C9H7NO");
    public static final Material Perbromothiophene = SimpleFluidMaterial("perbromothiophene", 0x87cc17, "C4Br4S");
    public static final Material Diethoxythiophene = SimpleFluidMaterial("dietoxythiophene", 0x90ff43, "C4H2(OC2H5)2S");
    public static final Material EDOT = SimpleFluidMaterial("ethylenedioxythiophene", 0x7a9996, "C2H4O2C4H2S");
    public static final Material CitricAcid = SimpleFluidMaterial("citric_acid", 0xffcc00, "C6H8O7");
    public static final Material OxalicAcid = SimpleFluidMaterial("oxalic_acid", 0x4aaae2, "HOOCCOOH");
    public static final Material Trimethylchlorosilane = SimpleFluidMaterial("trimethylchlorosilane", Dimethyldichlorosilane.getMaterialRGB(), "(CH3)3SiCl");
    public static final Material Dibromoacrolein = SimpleFluidMaterial("dibromoacrolein", 0x4a4a4a, "C2H2Br2O2");
    public static final Material Bromohydrothiine = SimpleFluidMaterial("bromodihydrothiine", 0x40ff3a, "C4H4S2Br2");
    public static final Material Bromobutane = SimpleFluidMaterial("bromobutane", 0xff3333, "CH3(CH2)3Br");
    public static final Material AstatideSolution = SimpleFluidMaterial("astatide_solution", 0x6df63f, "At(H2O)(SO3)");
    public static final Material Biperfluoromethanedisulfide = SimpleFluidMaterial("biperfluoromethanedisulfide", 0x3ada40, "C2F6S2");
    public static final Material BariumTriflateSolution = SimpleFluidMaterial("barium_triflate_solution", (Barium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, "(H2O)3(Hg)C2BaF6O6S2");
    public static final Material BariumStrontiumAcetateSolution = SimpleFluidMaterial("basr_acetate_solution", 0x9a9b98, "C2H3BaO2Sr");
    public static final Material TitaniumIsopropoxide = SimpleFluidMaterial("titanium_isopropoxide", 0xFF0066, "Ti(OCH(CH3)2)4");
    public static final Material BariumChlorideSolution = SimpleFluidMaterial("barium_chloride_solution", (Barium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, "(H2O)BaCl3");
    public static final Material IronCarbonyl = SimpleFluidMaterial("iron_carbonyl", 0xff8000, "Fe?");
    public static final Material PurifiedIronCarbonyl = SimpleFluidMaterial("purified_iron_carbonyl", 0xff8000, "Fe");
    public static final Material BismuthNitrateSoluton = SimpleFluidMaterial("bismuth_nitrate_solution", (Bismuth.getMaterialRGB()+Chlorine.getMaterialRGB())/2, "(H2O)Bi(NO3)3");
    public static final Material BariumTitanatePreparation = SimpleFluidMaterial("barium_titanate_preparation", 0x99FF99, "BaTiO3");
    public static final Material BariumStrontiumTitanatePreparation = SimpleFluidMaterial("basr_titanate_preparation", 0xFF0066, "(BaTiO3)C2H3BaO2Sr");
    public static final Material CarbonTetrachloride = SimpleFluidMaterial("carbon_tetrachloride", 0x2d8020, "CCl4");
    public static final Material Chloroethane = SimpleFluidMaterial("chloroethane", 0x33aa33, "CH3CH2Cl");
    public static final Material ActiniumSuperhydridePlasma = SimpleFluidMaterial("actinium_superhydride_plasma", Actinium.getMaterialRGB() * 9 / 8, "AcH12");
    public static final Material Diborane = SimpleFluidMaterial("diborane",Boron.getMaterialRGB(), "(BH3)2");
    public static final Material IsopropylAcetate = SimpleFluidMaterial("isopropyl_acetate", (Strontium.getMaterialRGB()+IsopropylAlcohol.getMaterialRGB()+Water.getMaterialRGB())/3, "(CH3)2CHCOOCH3");
    public static final Material ChlorinatedSolvents = SimpleFluidMaterial("chlorinated_solvents",0x40804c, "(CH4)2Cl5");
    public static final Material Dichloromethane = SimpleFluidMaterial("dichloromethane", Chloromethane.getMaterialRGB()-10, "CH2Cl2");
    public static final Material ButanolGas = SimpleFluidMaterial("butanol_gas",Butanol.getMaterialRGB()+20, "C4H9OH");
    public static final Material Tributylamine = SimpleFluidMaterial("tributylamine",0x801a80, "(C4H9)3N");
    public static final Material CrudeAluminaSolution = SimpleFluidMaterial("crude_alumina_solution", (Aluminium.getMaterialRGB()-30), "(Al(NO3)3)2(CH2Cl2)(C12H27N)");
    public static final Material AluminaSolution = SimpleFluidMaterial("alumina_solution", (Aluminium.getMaterialRGB()-15), "(Al2O3)(CH2Cl2)(C12H27N)2");
    public static final Material UnprocessedNdYAGSolution = SimpleFluidMaterial("unprocessed_ndyag_solution",0x6f20af, "Nd:YAG");
    public static final Material AmmoniumCyanate = SimpleFluidMaterial("ammonium_cyanate",0x3a5dcf, "NH4CNO");
    public static final Material Ethylenediamine = SimpleFluidMaterial("ethylenediamine", Ethanolamine.getMaterialRGB(), "C2H4(NH2)2");
    public static final Material EDTASolution = SimpleFluidMaterial("edta_solution",0x0026d9, "(C10H16N2O8)3(C2H8N2)O2");
    public static final Material EDTA = SimpleFluidMaterial("edta",0x0026d9, "C10H16N2O8");
    public static final Material Glycine = SimpleFluidMaterial("glycine", (Ethylenediamine.getMaterialRGB()+Formaldehyde.getMaterialRGB())/2, "NH2CH2COOH");
    public static final Material PrYHoNitrateSolution = SimpleFluidMaterial("pryho_nitrate_solution",0x00f2b2, "(Y(NO3)3)6(Pr(NO3)3)2(Nd(NO3)3)2(H2O)15");
    public static final Material PhosphorousArsenicSolution = SimpleFluidMaterial("phosphorous_arsenic_solution", PhosphoricAcid.getMaterialRGB(), "AsCd(HPO4)10");
    public static final Material FluorosilicicAcid = SimpleFluidMaterial("fluorosilicic_acid",0x2ccf2a, "H2SiF6");
    public static final Material AmmoniumFluoride = SimpleFluidMaterial("ammonium_fluoride",AmmoniumChloride.getMaterialRGB(), "NH4F");
    public static final Material AmmoniumBifluorideSolution = SimpleFluidMaterial("ammonium_bifluoride_solution", (Ammonia.getMaterialRGB()+Fluorine.getMaterialRGB())/2, "(H2O)NH4FHF");
    public static final Material LuTmYChlorideSolution = SimpleFluidMaterial("lutmy_chloride_solution",0x00f2b2, "(YCl3)6(LuCl3)2(TmCl3)2(H2O)15");
    public static final Material MercuryNitrate = SimpleFluidMaterial("mercury_nitrate",0xd6b8ad, "Hg(NO3)2");
    public static final Material BismuthVanadateSolution = SimpleFluidMaterial("bismuth_vanadate_solution",0xffff00, "(H2O)BiVO4");
    public static final Material Nitrotoluene = SimpleFluidMaterial("nitrotoluene",0xfcca00, "C7H7NO2");
    public static final Material Naphthylamine = SimpleFluidMaterial("naphthylamine",0xe3e81c, "C10H9N");
    public static final Material Acetoacetanilide = SimpleFluidMaterial("acetoacetanilide",0xffffc2, "C10H11NO2");
    public static final Material Quinizarin = SimpleFluidMaterial("quinizarin",0x3c5a2c0, "C14H8O4");
    public static final Material Toluenesulfonate = SimpleFluidMaterial("toluenesulfonate",0x8f8f00, "C7H7SO3Na");
    public static final Material Isopropylsuccinate = SimpleFluidMaterial("isopropylsuccinate",0xb26680, "C7H12O4");
    public static final Material MaleicAnhydride = SimpleFluidMaterial("maleic_anhydride",0x3c20ad, "C4H2O3");
    public static final Material Benzonitrile = SimpleFluidMaterial("benzonitrile",0x2c2c9c, "C7H5N");
    public static final Material SeaborgiumDopedNanotubes = SimpleFluidMaterial("seaborgium_doped_nanotubes",0x2c2c8c, "SgCNT");
    public static final Material FullereneDopedNanotubes = SimpleFluidMaterial("fullerene_doped_nanotubes",0x6c2c6c, "C60CNT");
    public static final Material AmmoniumNiobiumOxalateSolution = SimpleFluidMaterial("ammonium_niobium_oxalate_solution",0x6c6cac, "(NH4)C10Nb2O20");
    public static final Material DielectricMirrorFormationMix = SimpleFluidMaterial("dielectric_mirror_formation_mix",0xff992c, "MgF2ZnSTa2Ti(C2H6O8)");
    public static final Material LiquidZBLAN = SimpleFluidMaterial("molten_zblan",(Zirconium.getMaterialRGB()+Barium.getMaterialRGB()+Lanthanum.getMaterialRGB()+Aluminium.getMaterialRGB()+Fluorine.getMaterialRGB())/5, "(ZrF4)18(BaF2)7(LaF3)2(AlF3)(NaF)7");
    public static final Material ChlorousAcid = SimpleFluidMaterial("chlorous_acid", 0x2d6e8a, "HClO2"); // TODO USELESS, REMOVE
    public static final Material Iodobenzene = SimpleFluidMaterial("iodobenzene",0x2c2c6c0, "C6H5I");
    public static final Material Amino3phenol = SimpleFluidMaterial("3_aminophenol",Aminophenol.getMaterialRGB(), "C6H7NO");
    public static final Material Dimethylnaphthalene = SimpleFluidMaterial("dimethylnaphthalene",0xe34fb0, "C12H12");
    public static final Material IodineMonochloride = SimpleFluidMaterial("iodine_monochloride",0x004c4c, "ICl");
    public static final Material AcetylatingReagent = SimpleFluidMaterial("acetylating_reagent",0x8d5e63, "C9H12Si(MgBr)2");
    public static final Material Dihydroiodotetracene = SimpleFluidMaterial("dihydroiodotetracene",0x5c4d38, "H2C18H11I");
    public static final Material Dichlorodicyanobenzoquinone = SimpleFluidMaterial("dichlorodicyanobenzoquinone",0x3a2aba, "C8Cl2N2O2");
    public static final Material Dichlorodicyanohydroquinone = SimpleFluidMaterial("dichlorodicyanohidroquinone",0x3a2aba, "C8Cl2N2(OH)2");
    public static final Material IodobenzoicAcid = SimpleFluidMaterial("iodobenzoic_acid",0x2cac6c0, "C7H5IO2");
    public static final Material Methoxybenzaldehyde = SimpleFluidMaterial("methoxybenzaldehyde",0x3c3a7a, "C8H8O2");
    public static final Material Butylaniline = SimpleFluidMaterial("butylaniline", Aniline.getMaterialRGB(), "C10H15N");
    public static final Material MBBA = SimpleFluidMaterial("mbba",0xfa30fa, "C18H21NO");
    public static final Material LiquidCrystalDetector = SimpleFluidMaterial("liquid_crystal_detector",0xda20da);
    public static final Material PotassiumEthoxide = SimpleFluidMaterial("potassium_ethoxide",Ethanol.getMaterialRGB(), "C2H5KO");
    public static final Material TetraethylammoniumBromide = SimpleFluidMaterial("tetraethylammonium_bromide",0xcc33ff, "C8H20NBr");
    public static final Material Hexanediol = SimpleFluidMaterial("hexanediol", EthyleneGlycol.getMaterialRGB(), "C6H14O2");
    public static final Material Hexamethylenediamine = SimpleFluidMaterial("hexamethylenediamine",Ethylenediamine.getMaterialRGB(), "C6H16N2");
    public static final Material Tertbutanol = SimpleFluidMaterial("tertbutanol",0xcccc2c, "C4H10O");
    public static final Material Triaminoethaneamine = SimpleFluidMaterial("triaminoethaneamine",0x6f7d87, "(NH2CH2CH2)3N");
    public static final Material TertButylAzidoformate = SimpleFluidMaterial("tertbuthylcarbonylazide",0x888818, "C5H9N3O2");
    public static final Material AminatedFullerene = SimpleFluidMaterial("aminated_fullerene",0x2c2caa, "C60N12H12");
    public static final Material Azafullerene = SimpleFluidMaterial("azafullerene",0x8a7a1a, "C60N12H12");
    public static final Material Ethylamine = SimpleFluidMaterial("ethylamine",Ethylenediamine.getMaterialRGB(), "C2H5NH2");
    public static final Material Trimethylsilane = SimpleFluidMaterial("trimethylsilane",Trimethylchlorosilane.getMaterialRGB(), "C3H10Si");
    public static final Material KryptonDifluoride = SimpleFluidMaterial("krypton_difluoride",Krypton.getMaterialRGB(), "KrF2");
    public static final Material QuarkGluonPlasma = SimpleFluidMaterial("quark_gluon_plasma",0x8f00ff, "a" + "" + "(u2)d(c2)s(t2)bg" + "a", false, true);
    public static final Material HeavyQuarks = SimpleFluidMaterial("heavy_quarks",0x008800, "a" + "" + "(u2)ds" + "a", false, true);
    public static final Material LightQuarks = SimpleFluidMaterial("light_quarks",0x0000ff, "a" + "" + "(c2)(t2)b" + "a", false, true);
    public static final Material Gluons = SimpleFluidMaterial("gluons",0xfcfcfa, "a" + "" + "g" + "a", false, true);
    public static final Material HeavyLeptonMix = SimpleFluidMaterial("heavy_lepton_mix",0x5adf52, "a" + "" + "(t2)u" + "a", false, true);
    public static final Material CosmicComputingMix = SimpleFluidMaterial("cosmic_computing_mix",0xafad2f, "aaaaa", false, true);
    public static final Material HeavyQuarkEnrichedMix = SimpleFluidMaterial("heavy_quark_enriched_mix",0xefefef, "a" + "" + "(u2)d(c2)s(t2)b" + "a", false, true);
    public static final Material DeuteriumSuperheavyMix = SimpleFluidMaterial("deuterium_superheavy_mix",0xa2d2a4, "(H_2)FlHsOg");
    public static final Material ScandiumTitanium50Mix = SimpleFluidMaterial("scandium_titanium_50_mix", (Scandium.getMaterialRGB()+Titanium.getMaterialRGB())/2, "ScTi_50");
    public static final Material RadonRadiumMix = SimpleFluidMaterial("radon_radium_mix",(Radium.getMaterialRGB()+Radon.getMaterialRGB())/2, "RnRa");
    public static final Material Phenylsodium = SimpleFluidMaterial("phenylsodium",0x2c2cc8, "C6H5Na");
    public static final Material Difluoroaniline = SimpleFluidMaterial("difluoroaniline",0x3fac4a, "C6H5F2N");
    public static final Material Succinaldehyde = SimpleFluidMaterial("succinaldehyde",0x7c6d9a, "C4H6O2");
    public static final Material NDifluorophenylpyrrole = SimpleFluidMaterial("n_difluorophenylpyrrole",0x3a9aa9, "C10H7F2N");
    public static final Material PhotopolymerSolution = SimpleFluidMaterial("photopolymer_solution",0x8a526d, "C149H97N10O2(TiBF20)");
    public static final Material Trichloroferane = SimpleFluidMaterial("trichloroferane",0x521973, "FlCl3");
    public static final Material GlucoseIronSolution = SimpleFluidMaterial("glucose_iron_solution", (Sugar.getMaterialRGB()+Iron.getMaterialRGB())/2, "(C6H12O6)FeCl3");
    public static final Material GrapheneOxidationSolution = SimpleFluidMaterial("graphene_oxidation_solution",0x96821a, "(KMnO4)(NaNO3)(H2SO4)");
    public static final Material SupercriticalCO2 = SimpleFluidMaterial("supercritcal_co_2",CarbonDioxide.getMaterialRGB(), "CO2");
    public static final Material NobleGases = SimpleFluidMaterial("noble_gases_mixture",(Helium.getMaterialRGB()+Neon.getMaterialRGB()+Argon.getMaterialRGB()+Krypton.getMaterialRGB()+Xenon.getMaterialRGB()+Radon.getMaterialRGB()), "HeNeArKrXeRn");
    public static final Material NonMetals = SimpleFluidMaterial("non_metals",(Hydrogen.getMaterialRGB()+Boron.getMaterialRGB()+Carbon.getMaterialRGB()+Nitrogen.getMaterialRGB()+Oxygen.getMaterialRGB()+Fluorine.getMaterialRGB()+Phosphorus.getMaterialRGB()+Sulfur.getMaterialRGB()+Chlorine.getMaterialRGB()+Arsenic.getMaterialRGB()+Selenium.getMaterialRGB()+Bromine.getMaterialRGB()+Tellurium.getMaterialRGB()+Iodine.getMaterialRGB()+Astatine.getMaterialRGB()), "BCPSAsSeTeIAtONHFClBr");
    public static final Material DenseNeutronPlasma = SimpleFluidMaterial("dense_neutron_plasma",0xacecac,1000000, false, "a" + "n" + "a");
    public static final Material CosmicMeshPlasma = SimpleFluidMaterial("cosmic_mesh_plasma",0x1c1c8c,1000000, false, "nn");
    public static final Material SuperfluidHelium = SimpleFluidMaterial("superfluid_helium",Helium.getMaterialRGB(),2, false, "He");
    public static final Material LiquidHelium3 = SimpleFluidMaterial("liquid_helium_3", Helium3.getMaterialRGB(),4, false, "He_3");
    public static final Material LiquidEnrichedHelium = SimpleFluidMaterial("liquid_enriched_helium", Helium.getMaterialRGB(), 4, false, "HeHe_3");
    public static final Material LiquidNitrogen = SimpleFluidMaterial("liquid_nitrogen",Nitrogen.getMaterialRGB(), 70, false, "N");
    public static final Material Methylethanolamine = SimpleFluidMaterial("methylethanolamine",0x6a3baa, "C3H9NO");
    public static final Material Methylguanidine = SimpleFluidMaterial("methylguanidine",0x5a9a3c, "C2H7N3");
    public static final Material Methylnitronitrosoguanidine = SimpleFluidMaterial("methylnitronitrosoguanidine",0x68b15d, "C2H5N5O3");
    public static final Material IsoamylAlcohol = SimpleFluidMaterial("isoamyl_alcohol",0xcaba77, "C5H12O");
    public static final Material Octanol = SimpleFluidMaterial("octanol",0xa2b8c2, "C8H18O");
    public static final Material Trioctylamine = SimpleFluidMaterial("trioctylamine",0x87a2bc, "C24H51N");
    public static final Material RheniumSeparationMixture = SimpleFluidMaterial("rhenium_separation_mixture",0xed2c3a, "C11H24");
    public static final Material RheniumScrubbedSolution = SimpleFluidMaterial("rhenium_scrubbed_solution",0xedccca, "Re?");
    public static final Material NeutroniumDopedNanotubes = SimpleFluidMaterial("neutronium_doped_nanotubes",(Neutronium.getMaterialRGB()+CarbonNanotubes.getMaterialRGB())/2, "Nt?");
    public static final Material SupercriticalSteam = SimpleFluidMaterial("supercritical_steam", Steam.getMaterialRGB(), "H2O");
    public static final Material SupercriticalDeuterium = SimpleFluidMaterial("supercritical_deuterium", Deuterium.getMaterialRGB(), "H_2");
    public static final Material SupercriticalSodiumPotassiumAlloy = SimpleFluidMaterial("supercritical_sodium_potassium_alloy", SodiumPotassiumAlloy.getMaterialRGB(), "Na7K3");
    public static final Material SupercriticalSodium = SimpleFluidMaterial("supercritical_sodium", Sodium.getMaterialRGB(), "Na");
    public static final Material SupercriticalFLiNaK = SimpleFluidMaterial("supercritical_flinak", FLiNaK.getMaterialRGB(), "FLiNaK");
    public static final Material SupercriticalFLiBe = SimpleFluidMaterial("supercritical_flibe", FLiBe.getMaterialRGB(), "FLiBe");
    public static final Material SupercriticalLeadBismuthEutectic = SimpleFluidMaterial("supercritical_lead_bismuth_eutectic", LeadBismuthEutectic.getMaterialRGB(), "Pb3Bi7");
    public static final Material FreeAlphaGas = SimpleFluidMaterial("free_alpha_gas", 0xe0d407, "a");
    public static final Material FreeElectronGas = SimpleFluidMaterial("free_electron_gas", 0x044c4c, "e-");
    public static final Material HighEnergyQGP = SimpleFluidMaterial("high_energy_qgp", 0x8f00ff, "a" + "" + "(u2)d(c2)s(t2)bg" + "a", false, true);
    public static final Material AcetylsulfanilylChloride = SimpleFluidMaterial("acetylsulfanilyl_chloride", (Aniline.getMaterialRGB() + AceticAnhydride.getMaterialRGB() + ChlorosulfonicAcid.getMaterialRGB())/3, "C8H8ClNO3S");
    public static final Material BenzoylPeroxide = SimpleFluidMaterial("benzoyl_peroxide", (Barium.getMaterialRGB() + BenzoylChloride.getMaterialRGB())/2, "C14H10O4");
    public static final Material Iron2Chloride = SimpleFluidMaterial("iron_ii_chloride", (IronChloride.getMaterialRGB()-10), "FeCl2");
    public static final Material Propadiene = SimpleFluidMaterial("propadiene", (Butanol.getMaterialRGB()-20), "C3H4");
    public static final Material FluorophosphoricAcid = SimpleFluidMaterial("fluorophosphoric_acid", PhosphorusTrichloride.getMaterialRGB(), "HPF6");
    public static final Material PhenylenedioxydiaceticAcid = SimpleFluidMaterial("phenylenedioxydiacetic_acid", 0x99546a, "C10H10O6");
    public static final Material Diethylthiourea = SimpleFluidMaterial("diethylthiourea", 0x2acaa4, "(C2H5NH)2CS");
    public static final Material Isophthaloylbisdiethylthiourea = SimpleFluidMaterial("isophthaloylbisdiethylthiourea", 0x8a7b9c, "C18H26N4O2S2");
    public static final Material SodiumAlginateSolution = SimpleFluidMaterial("sodium_alginate_solution",0xca8642, "NaC6H7O6");
    public static final Material AscorbicAcid = SimpleFluidMaterial("ascorbic_acid",0xe6cd00, "C6H8O6");
    public static final Material DehydroascorbicAcid = SimpleFluidMaterial("dehydroascorbic_acid",0xe6cd00, "C6H6O6");
    public static final Material CaCBaSMixture = SimpleFluidMaterial("cacbas_mixture", (CalciumCarbonateSolution.getMaterialRGB() + BariumSulfateSolution.getMaterialRGB()) / 2);
    public static final Material LubricantClaySlurry = SimpleFluidMaterial("lubricant_clay_slurry", (Lubricant.getMaterialRGB() + BentoniteClaySlurry.getMaterialRGB()) / 2);
    public static final Material ATLEthylene = SimpleFluidMaterial("atl_ethylene_mixture", (ATL.getMaterialRGB() + EthyleneGlycol.getMaterialRGB()) / 2);
    public static final Material DrillingMudMixture = SimpleFluidMaterial("drilling_mud_mixture", (CaCBaSMixture.getMaterialRGB() + LubricantClaySlurry.getMaterialRGB()) / 2);
    public static final Material Cyclopentadiene = SimpleFluidMaterial("cyclopentadiene", Cyclooctadiene.getMaterialRGB(), "C5H6");
    public static final Material ChloroauricAcid = SimpleFluidMaterial("chloroauric_acid", 0xDFD11F, "HAuCl?");
    public static final Material Helium4 = SimpleFluidMaterial("helium_4", Helium.getMaterialRGB()-10, "He_4");
    public static final Material FermionicUUMatter = SimpleFluidMaterial("fermionic_uu_matter", UUMatter.getMaterialRGB() / 3, "???");
    public static final Material BosonicUUMatter = SimpleFluidMaterial("bosonic_uu_matter", UUMatter.getMaterialRGB() - FermionicUUMatter.getMaterialRGB(), "???");
    public static final Material Oxydianiline = SimpleFluidMaterial("oxydianiline", 0xF0E130, "C12H12N2O");
    public static final Material PolyamicAcid = SimpleFluidMaterial("polyamic_acid", 0xFFAE42, "C22H14N2O7");
    public static final Material Hexafluoropropylene = SimpleFluidMaterial("hexafluoropropylene", 0x111111, "C3F6");
    public static final Material Dimethylether = SimpleFluidMaterial("dimethylether", 0xe6cd11, "C2H6O");
    public static final Material Dimethoxyethane = SimpleFluidMaterial("dimethoxyethane", 0x2acbb4, "C4H10O2");
    public static final Material LithiumCyclopentadienide = SimpleFluidMaterial("lithiumcyclopentadienide", 0x95556a, "LiC5H5");
    public static final Material CaliforniumCyclopentadienide = SimpleFluidMaterial("californiumcyclopentadienide", 0x94445b, "C15H15Cf");
    public static final Material Soap = SimpleFluidMaterial("soap", 0xFFAE42, "?");
    public static final Material DeglyceratedSoap = SimpleFluidMaterial("deglyceratedsoap", 0xFFAE41);
    public static final Material StearicAcid = SimpleFluidMaterial("stearicacid", 0x2bbbb4, "C18H36O2");
    public static final Material Trioctylphosphine = SimpleFluidMaterial("trioctylphosphine", 0xF1E130, "C24H51P");
    public static final Material QuantumDots = SimpleFluidMaterial("quantumdots", 0xff0000, "CdSe", true);
    public static final Material IridiumTrichlorideSolution = SimpleFluidMaterial("iridiumtrichloridesolution", 0x96821a, "IrCl3");
    public static final Material SemisolidHydrogen = SimpleFluidMaterial("semisolidhydrogen", 0x044c4b, "H");
    public static final Material MicrocrystallizingHydrogen = SimpleFluidMaterial("microcrystallizinghydrogen", 0x155d5c, "H");
    public static final Material Toluidine = SimpleFluidMaterial("toluidine",(Toluene.getMaterialRGB()+ Aniline.getMaterialRGB())/2,"C7H9N");
    public static final Material ApatiteAcidicLeach = SimpleFluidMaterial("apatite_acidic_leach", PhosphoricAcid.getMaterialRGB(), "H10P3O12Cl??");
    public static final Material FluoroapatiteAcidicLeach = SimpleFluidMaterial("fluoroapatite_acidic_leach", PhosphoricAcid.getMaterialRGB(),"H10P3O12F??");
    public static final Material NitrousAcid = SimpleFluidMaterial("nitrous_acid", 0x1e73b0, "HNO2");
    public static final Material HydroxylamineHydrochloride = SimpleFluidMaterial("hydroxylamine_hydrochloride", ((Barium.getMaterialRGB()+Chlorine.getMaterialRGB())/2 + 0xF0EAD6)/2, "HONH2HCl");
    public static final Material SelenousAcid = SimpleFluidMaterial("selenous_acid", (0xFFFF66 + Water.getMaterialRGB())/2, "H2SeO3");
    public static final Material Glyoxal = SimpleFluidMaterial("glyoxal", 0xf2f068, "C2H2O2");
    public static final Material BenzylChloride = SimpleFluidMaterial("benzyl_chloride", 0xaef7fc, "C7H7Cl");
    public static final Material Benzylamine = SimpleFluidMaterial("benzylamine", 0x5c8082, "C7H9N");
    public static final Material Tetrahydrofuran = SimpleFluidMaterial("tetrahydrofuran", 0xb7ebcd, "(CH2)4O");
    public static final Material Triethylamine = SimpleFluidMaterial("triethylamine", Ethylenediamine.getMaterialRGB(), "N(CH2CH3)3");
    public static final Material TetrafluoroboricAcid = SimpleFluidMaterial("tetrafluoroboric_acid",Silvertetrafluoroborate.getMaterialRGB(),"HBF4");
    public static final Material BoronTrifluorideEtherate = SimpleFluidMaterial("boron_trifluoride_etherate", (BoronFluoride.getMaterialRGB()+Diethylether.getMaterialRGB())/2, "(BF3)(C2H5)2O");
    public static final Material BoraneDimethylsulfide = SimpleFluidMaterial("borane_dimethylsulfide", (Diborane.getMaterialRGB()+Dimethylsulfide.getMaterialRGB())/2, "(BH3)(CH3)2S");
    public static final Material Perfluorobenzene = SimpleFluidMaterial("perfluorobenzene", 0x226E22, "C6F6");
    public static final Material NitratedTriniiteSolution = SimpleFluidMaterial("nitrated_triniite_solution", 0x428c9f);
    public static final Material ResidualTriniiteSolution = SimpleFluidMaterial("residual_triniite_solution", 0x219daf);
    public static final Material HeavilyFluorinatedTriniumSolution = SimpleFluidMaterial("heavily_fluorinated_trinium_solution", 0x348d41);
    public static final Material MoltenCalciumSalts = SimpleFluidMaterial("molten_calcium_salts",(Fluorite.getMaterialRGB()+Calcium.getMaterialRGB())/2);
    public static final Material EthylTrifluoroacetate = SimpleFluidMaterial("ethyl_trifluoroacetate", 0x88a12d, "C4H5F3O2");
    public static final Material Acetothienone = SimpleFluidMaterial("acetothieone", 0x79882a, "C6H6SO");
    public static final Material TheonylTrifluoroacetate = SimpleFluidMaterial("theonyl_trifluoroacetate",0x88882b, "C8H5F3O2S");
    public static final Material ActiniumRadiumNitrateSolution = SimpleFluidMaterial("actinium_radium_nitrate_solution", 0xd2f0df);
    public static final Material ActiniumRadiumHydroxideSolution = SimpleFluidMaterial("actinium_radium_hydroxide_solution", 0xe2f5ef);
    public static final Material FumingNitricAcid = SimpleFluidMaterial("fuming_nitric_acid", NitricAcid.getMaterialRGB(), "HNO3");
    public static final Material AcetylChloride = SimpleFluidMaterial("acetyl_chloride", AceticAcid.getMaterialRGB(), "C2H3OCl");
    public static final Material DirtyHexafluorosilicicAcid = SimpleFluidMaterial("dirty_hexafluorosilicic_acid",(Stone.getMaterialRGB()+FluorosilicicAcid.getMaterialRGB()/2), "H2SiF6?");
    public static final Material DiluteHexafluorosilicicAcid = SimpleFluidMaterial("dilute_hexafluorosilicic_acid", (Water.getMaterialRGB()*2+FluorosilicicAcid.getMaterialRGB())/3,"(H2O)2(H2SiF6)");
    public static final Material Dioxygendifluoride = SimpleFluidMaterial("dioxygen_difluoride", 0x32bdaf, "FOOF");
    public static final Material DiluteHydrofluoricAcid = SimpleFluidMaterial("dilute_hydrofluoric_acid", (Water.getMaterialRGB()+HydrofluoricAcid.getMaterialRGB())/3,"(H2O)(HF)");
    public static final Material OxidizedResidualSolution = SimpleFluidMaterial("oxidized_residual_solution", 0x23ad7f);
    public static final Material TritiumHydride = SimpleFluidMaterial("tritium_hydride",Tritium.getMaterialRGB(), "TH");
    public static final Material Helium3Hydride = SimpleFluidMaterial("helium_iii_hydride", Helium3.getMaterialRGB(), "He_3H");
    public static final Material UltraacidicResidueSolution = SimpleFluidMaterial("ultraacidic_residue_solution", (FluoroantimonicAcid.getMaterialRGB()+Helium3Hydride.getMaterialRGB())/2);
    public static final Material XenicAcid = SimpleFluidMaterial("xenic_acid", 0x5a4c9c, "H2XeO4");
    public static final Material DustyLiquidHelium3 = SimpleFluidMaterial("dusty_liquid_helium_3", 2*Helium3.getMaterialRGB()/3+Taranium.getMaterialRGB()/5);
    public static final Material TaraniumEnrichedLHelium3 = SimpleFluidMaterial("taranium_enriched_liquid_helium_3", Helium3.getMaterialRGB()/2+Taranium.getMaterialRGB()/2);
    public static final Material TaraniumSemidepletedLHelium3 = SimpleFluidMaterial("taranium_semidepleted_liquid_helium_3", 2*Helium3.getMaterialRGB()/3+Taranium.getMaterialRGB()/5);
    public static final Material TaraniumDepletedLHelium3 = SimpleFluidMaterial("taranium_depleted_liquid_helium_3",Helium3.getMaterialRGB()*5/6+Taranium.getMaterialRGB()/8);
    public static final Material TaraniumRichDustyHeliumPlasma = SimpleFluidMaterial("taranium_rich_dusty_helium_plasma", Helium.getMaterialRGB()/2+Taranium.getMaterialRGB()/2, 10000);
    public static final Material TaraniumDepletedHeliumPlasma = SimpleFluidMaterial("taranium_depleted_helium_plasma", Helium.getMaterialRGB()/2+Taranium.getMaterialRGB()/2, 10000);
    public static final Material TaraniumRichHelium4 = SimpleFluidMaterial("taranium_rich_helium_4", Helium.getMaterialRGB()/2+Taranium.getMaterialRGB(), true);
    public static final Material TaraniumPoorLiquidHelium = SimpleFluidMaterial("taranium_poor_liquid_helium", Helium3.getMaterialRGB()*6/7+Taranium.getMaterialRGB()/14);
    public static final Material LiquidFluorine = SimpleFluidMaterial("liquid_fluorine",Fluorine.getMaterialRGB()-0x303030);
    public static final Material LiquidXenon = SimpleFluidMaterial("liquid_xenon",Xenon.getMaterialRGB(), 165);
    public static final Material TaraniumPoorLiquidHeliumMix = SimpleFluidMaterial("taranium_poor_liquid_helium_mix", TaraniumPoorLiquidHelium.getMaterialRGB()*10/11+Helium.getMaterialRGB()/11);
    public static final Material HydroiodicAcid = SimpleFluidMaterial("hydroiodic_acid",Hydrogen.getMaterialRGB()/2+Iodine.getMaterialRGB()/2, "HI");
    public static final Material ChlorodiisopropylPhosphine = SimpleFluidMaterial("chlorodiisopropyl_phosphine", 0xa2c122);
    public static final Material CesiumBromideSolution = SimpleFluidMaterial("cesium_bromide_solution", (Caesium.getMaterialRGB()-10+SaltWater.getMaterialRGB())/2, "CsBr(H2O)");



    // simple dusts

    public static Material SimpleDustMaterial(String name, int rgb, short id, MaterialIconSet materialIconSet, ImmutableList<MaterialStack> materialComponents) {
        return new Builder(id(name))
                .dust()
                .color(rgb)
                .iconSet(materialIconSet)
                .componentStacks(materialComponents)
                .buildAndRegister();
    }

    public static Material SimpleDustMaterial(String name, int rgb, short id, MaterialIconSet materialIconSet) {
        return new Builder(id(name))
                .dust()
                .color(rgb)
                .iconSet(materialIconSet)
                .buildAndRegister();
    }

    public static Material SimpleDustMaterial(String name, int rgb, short id, MaterialIconSet materialIconSet, String formula) {
        return new Builder(id(name))
                .dust()
                .color(rgb)
                .iconSet(materialIconSet)
                .buildAndRegister().setFormula(formula);
    }

    public static Material SimpleDustMaterial(String name, int rgb, short id, MaterialIconSet materialIconSet, String formula, boolean fancy) {
        return new Builder(id(name))
                .dust()
                .color(rgb)
                .iconSet(materialIconSet)
                .buildAndRegister().setFormula(formula);
    }

    public static final Material IndiumTrifluoride = SimpleDustMaterial("indium_trifluoride", 0x2b0f48, (short) 1, Indium.getMaterialIconSet(), "InF3");
    public static final Material IndiumTrioxide = SimpleDustMaterial("indium_trioxide", 0x2b0f48, (short) 2, Indium.getMaterialIconSet(), "In2O3");
    public static final Material NaquadahConcentrate = SimpleDustMaterial("naquadah_concentrate", Naquadah.getMaterialRGB(), (short) 3, Naquadah.getMaterialIconSet(), "Nq?");
    public static final Material EnrichedNaquadahConcentrate = SimpleDustMaterial("enriched_naquadah_concentrate", NaquadahEnriched.getMaterialRGB(), (short) 4, NaquadahEnriched.getMaterialIconSet(), "Nq+?");
    public static final Material NaquadriaConcentrate = SimpleDustMaterial("naquadria_concentrate", Naquadria.getMaterialRGB(), (short) 5, Naquadria.getMaterialIconSet(), "*Nq*?");
    public static final Material AuricFluoride = SimpleDustMaterial("auric_fluoride", 0xdffb50, (short) 6, MaterialIconSet.SHINY, "AuF3");
    public static final Material ThUSludge = SimpleDustMaterial("thorium_uranium_sludge", 0x002908, (short) 7, MaterialIconSet.DULL, "ThU");
    public static final Material LanthanumOxide = SimpleDustMaterial("lanthanum_oxide", Lanthanum.getMaterialRGB(), (short) 8, Lanthanum.getMaterialIconSet(), "La2O3");
    public static final Material PraseodymiumOxide = SimpleDustMaterial("praseodymium_oxide", Praseodymium.getMaterialRGB(), (short) 9, Praseodymium.getMaterialIconSet(), "Pr2O3");
    public static final Material NeodymiumOxide = SimpleDustMaterial("neodymium_oxide", Neodymium.getMaterialRGB(), (short) 10, Neodymium.getMaterialIconSet(), "Nd2O3");
    public static final Material CeriumOxide = SimpleDustMaterial("cerium_oxide", Cerium.getMaterialRGB(), (short) 11, Cerium.getMaterialIconSet(), "Ce2O3");
    public static final Material EuropiumOxide = SimpleDustMaterial("europium_oxide", Europium.getMaterialRGB(), (short) 12, Europium.getMaterialIconSet(), "Eu2O3");
    public static final Material GadoliniumOxide = SimpleDustMaterial("gadolinium_oxide", Gadolinium.getMaterialRGB(), (short) 13, Gadolinium.getMaterialIconSet(), "Gd2O3");
    public static final Material SamariumOxide = SimpleDustMaterial("samarium_oxide", Samarium.getMaterialRGB(), (short) 14, Samarium.getMaterialIconSet(), "Sm2O3");
    public static final Material TerbiumOxide = SimpleDustMaterial("terbium_oxide", Terbium.getMaterialRGB(), (short) 15, Terbium.getMaterialIconSet(), "Tb2O3");
    public static final Material DysprosiumOxide = SimpleDustMaterial("dysprosium_oxide", Dysprosium.getMaterialRGB(), (short) 16, Dysprosium.getMaterialIconSet(), "Dy2O3");
    public static final Material HolmiumOxide = SimpleDustMaterial("holmium_oxide", Holmium.getMaterialRGB(), (short) 17, Holmium.getMaterialIconSet(), "Ho2O3");
    public static final Material ErbiumOxide = SimpleDustMaterial("erbium_oxide", Erbium.getMaterialRGB(), (short) 18, Erbium.getMaterialIconSet(), "Er2O3");
    public static final Material ThuliumOxide = SimpleDustMaterial("thulium_oxide", Thulium.getMaterialRGB(), (short) 19, Thulium.getMaterialIconSet(), "Tm2O3");
    public static final Material YtterbiumOxide = SimpleDustMaterial("ytterbium_oxide", Ytterbium.getMaterialRGB(), (short) 20, Ytterbium.getMaterialIconSet(), "Yb2O3");
    public static final Material LutetiumOxide = SimpleDustMaterial("lutetium_oxide", Lutetium.getMaterialRGB(), (short) 21, Lutetium.getMaterialIconSet(), "Lu2O3");
    public static final Material ScandiumOxide = SimpleDustMaterial("scandium_oxide", Scandium.getMaterialRGB(), (short) 22, Scandium.getMaterialIconSet(), "Sc2O3");
    public static final Material CalciumCarbide = SimpleDustMaterial("calcium_carbide", 0x807b70, (short) 23, MaterialIconSet.DULL, "CaC2");
    public static final Material CalciumHydroxide = SimpleDustMaterial("calcium_hydroxide", 0x5f8764, (short) 24, MaterialIconSet.DULL, "Ca(OH)2");
    public static final Material BetaPinene = SimpleDustMaterial("beta_pinene", 0x61ad6b, (short) 25, MaterialIconSet.DULL, "C10H16");
    public static final Material Yeast = SimpleDustMaterial("yeast", 0xf0e660, (short) 26, MaterialIconSet.ROUGH, "???");
    public static final Material Glutamine = SimpleDustMaterial("glutamine", 0xede9b4, (short) 27, MaterialIconSet.DULL,"C5H10N2O3");
    public static final Material SilicaGel = SimpleDustMaterial("silica_gel", 0x61daff, (short) 28, MaterialIconSet.SHINY, "SiO2");
    public static final Material SilicaAluminaGel = SimpleDustMaterial("silica_alumina_gel", 0x558d9e, (short) 29, MaterialIconSet.ROUGH, "Al2O3SiO2");
    public static final Material ZeoliteSievingPellets = SimpleDustMaterial("zeolite_sieving_pellets", 0xa17bd1, (short) 30, MaterialIconSet.ROUGH, "Al2O3SiO2");
    public static final Material WetZeoliteSievingPellets = SimpleDustMaterial("wet_zeolite_sieving_pellets", 0x392f45, (short) 31, MaterialIconSet.METALLIC, "Al2O3SiO2?");
    public static final Material GreenAlgae = SimpleDustMaterial("green_algae", 0x228b22, (short) 32, MaterialIconSet.METALLIC, "An Algae");
    public static final Material BrownAlgae = SimpleDustMaterial("brown_algae", 0xa52a2a, (short) 33, MaterialIconSet.METALLIC, "An Algae");
    public static final Material RedAlgae = SimpleDustMaterial("red_algae", 0xf08080, (short) 34, MaterialIconSet.METALLIC, "An Algae");
    public static final Material DryRedAlgae = SimpleDustMaterial("dry_red_algae", 0xff7f50, (short) 35, MaterialIconSet.ROUGH, "A Dry Algae");
    public static final Material RedAlgaePowder = SimpleDustMaterial("red_algae_powder", 0xcc2f2f, (short) 36, MaterialIconSet.ROUGH, "A Powdered Algae");
    public static final Material PreFreezeAgar = SimpleDustMaterial("pre_freeze_agar", 0x132b0d, (short) 37, MaterialIconSet.ROUGH, "Warm Agar");
    public static final Material FrozenAgarCrystals = SimpleDustMaterial("frozen_agar_crystals", 0x68db4b, (short) 38, MaterialIconSet.SHINY, "Cold Agar");
    public static final Material BrevibacteriumFlavium = SimpleDustMaterial("brevibacterium_flavium", 0x2c4d24, (short) 40, MaterialIconSet.ROUGH, "Bacteria");
    public static final Material StreptococcusPyogenes = SimpleDustMaterial("streptococcus_pyogenes", 0x1c3b15, (short) 41, MaterialIconSet.ROUGH, "Bacteria");
    public static final Material EschericiaColi = SimpleDustMaterial("eschericia_coli", 0x2d4228, (short) 42, MaterialIconSet.ROUGH, "Bacteria");
    public static final Material BifidobacteriumBreve = SimpleDustMaterial("bifidobacterium_breve", 0x377528, (short) 43, MaterialIconSet.ROUGH, "Bacteria");
    public static final Material Alumina = SimpleDustMaterial("alumina", 0x0b585c, (short) 44, MaterialIconSet.ROUGH, "Al2O3");
    public static final Material CupriavidusNecator = SimpleDustMaterial("cupriavidus_necator", 0x22704f, (short) 46, MaterialIconSet.ROUGH, "Bacteria");
    public static final Material Shewanella = SimpleDustMaterial("shewanella", 0x8752ab, (short) 47, MaterialIconSet.METALLIC, "Bacteria");
    public static final Material ZirconiumTetrachloride = SimpleDustMaterial("zirconium_tetrachloride", 0xF0F0F0, (short) 54, MaterialIconSet.SHINY, "ZrCl4");
    public static final Material SiliconCarbide = SimpleDustMaterial("silicon_carbide", (Silicon.getMaterialRGB()+Carbon.getMaterialRGB())/2, (short) 55, MaterialIconSet.SHINY, "SiC");
    public static final Material GoldDepleteMolybdenite = SimpleDustMaterial("gold_deplete_molybdenite", 0x7c7c8f, (short) 48, MaterialIconSet.SHINY, "MoS2?");
    public static final Material MolybdenumConcentrate = SimpleDustMaterial("molybdenum_concentrate", 0x565666, (short) 49, MaterialIconSet.SHINY, "MoS2Re");
    public static final Material MolybdenumTrioxide = SimpleDustMaterial("molybdenum_trioxide", 0x666685, (short) 50, MaterialIconSet.SHINY, "MoO3");
    public static final Material CopperChloride = SimpleDustMaterial("copper_chloride", 0xf5b35d, (short) 51, MaterialIconSet.SHINY, "CuCl2");
    public static final Material BismuthChloride = SimpleDustMaterial("bismuth_chloride", 0x95f5d7, (short) 52, MaterialIconSet.SHINY, "BiCl3");
    public static final Material LeadChloride = SimpleDustMaterial("lead_chloride", (Lead.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 53, MaterialIconSet.SHINY, "PbCl2");
    public static final Material ZirconiumTetrafluoride = SimpleDustMaterial("zirconium_tetrafluoride", (Zirconium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 56, MaterialIconSet.SHINY, "ZrF4");
    public static final Material BariumDifluoride = SimpleDustMaterial("barium_difluoride", (Barium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 57, MaterialIconSet.METALLIC, "BaF2");
    public static final Material LanthanumTrifluoride = SimpleDustMaterial("lanthanum_trifluoride", (Lanthanum.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 58, MaterialIconSet.METALLIC, "LaF3");
    public static final Material AluminiumTrifluoride = SimpleDustMaterial("aluminium_trifluoride", (Aluminium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 59, MaterialIconSet.ROUGH, "AlF3");
    public static final Material ErbiumTrifluoride = SimpleDustMaterial("erbium_trifluoride", (Erbium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 60, MaterialIconSet.ROUGH, "ErF3");
    public static final Material ZBLANDust = SimpleDustMaterial("zblan_dust", (ZirconiumTetrafluoride.getMaterialRGB()+BariumDifluoride.getMaterialRGB()+LanthanumTrifluoride.getMaterialRGB()+AluminiumTrifluoride.getMaterialRGB())/4, (short) 61, MaterialIconSet.SHINY, "(ZrF4)18(BaF2)7(LaF3)2(AlF3)(NaF)7");
    public static final Material ErbiumDopedZBLANDust = SimpleDustMaterial("erbium_doped_zblan_dust", (ZBLANDust.getMaterialRGB()+ErbiumTrifluoride.getMaterialRGB())/2, (short) 62, MaterialIconSet.SHINY, "(ErF3)(ZrF4)18(BaF2)7(LaF3)2(AlF3)(NaF)7");
    public static final Material PotassiumCyanide = SimpleDustMaterial("potassium_cyanide", (Potassium.getMaterialRGB()+Nitrogen.getMaterialRGB())/2, (short) 63, MaterialIconSet.ROUGH, "KCN");
    public static final Material SuccinicAcid = SimpleDustMaterial("succinic_acid", (MaleicAnhydride.getMaterialRGB()+Water.getMaterialRGB()+Hydrogen.getMaterialRGB())/3, (short) 64, MaterialIconSet.ROUGH, "C4H6O4");
    public static final Material Succinimide = SimpleDustMaterial("succinimide", (SuccinicAcid.getMaterialRGB()+Ammonia.getMaterialRGB())/2, (short) 65, MaterialIconSet.METALLIC, "C4H5NO2");
    public static final Material Bromosuccinimide = SimpleDustMaterial("bromo_succinimide", (Succinimide.getMaterialRGB()+Bromine.getMaterialRGB())/2, (short) 66, MaterialIconSet.METALLIC, "C4H4BrNO2");
    public static final Material Benzophenanthrenylacetonitrile = SimpleDustMaterial("benzophenanthrenylacetonitrile", (Naphthaldehyde.getMaterialRGB()+Ethylene.getMaterialRGB()-20)/2, (short) 67, MaterialIconSet.ROUGH, "C20H13N");
    public static final Material UnfoldedFullerene = SimpleDustMaterial("unfolded_fullerene", (Benzophenanthrenylacetonitrile.getMaterialRGB()+Oxygen.getMaterialRGB())/2, (short) 68, MaterialIconSet.DULL, "C60H30");
    public static final Material Fullerene = SimpleDustMaterial("fullerene", (UnfoldedFullerene.getMaterialRGB()-20), (short) 69, MaterialIconSet.METALLIC, "C60");
    public static final Material TiAlChloride = SimpleDustMaterial("tial_chloride", (Titanium.getMaterialRGB()+Aluminium.getMaterialRGB()+Chlorine.getMaterialRGB())/3, (short) 70, MaterialIconSet.DULL, "TiAlCl7");
    public static final Material Dimethylaminopyridine = SimpleDustMaterial("dimethylaminopyridine", (Dimethylamine.getMaterialRGB()+Pyridine.getMaterialRGB())/2, (short) 71, MaterialIconSet.ROUGH, "(CH3)2NC5H4N");
    public static final Material PdIrReOCeOS = SimpleDustMaterial("pdirreoceos", (Palladium.getMaterialRGB()+Iridium.getMaterialRGB()+Rhenium.getMaterialRGB()+Cerium.getMaterialRGB()+Osmium.getMaterialRGB()+Silicon.getMaterialRGB()+Oxygen.getMaterialRGB())/7, (short) 72, MaterialIconSet.SHINY, "PdIrReCeOsSiO4");
    public static final Material SodiumEthoxide = SimpleDustMaterial("sodium_ethoxide", (Ethanol.getMaterialRGB()+ SodiumHydroxide.getMaterialRGB())/2, (short) 73, MaterialIconSet.METALLIC, "C2H5ONa");
    public static final Material MgClBromide = SimpleDustMaterial("mgcl_bromide", (MagnesiumChloride.getMaterialRGB()+Bromine.getMaterialRGB())/2, (short) 74, MaterialIconSet.ROUGH, "MgClBr");
    public static final Material Sarcosine = SimpleDustMaterial("sarcosine", (Glycine.getMaterialRGB()+Oxygen.getMaterialRGB())/2, (short) 75, MaterialIconSet.SHINY, "C3H7NO2");
    public static final Material SodiumNitrite = SimpleDustMaterial("sodium_nitrite", (Sodium.getMaterialRGB()+Nitrogen.getMaterialRGB())/2, (short) 76, MaterialIconSet.ROUGH, "NaNO2");
    public static final Material ZnFeAlClCatalyst = SimpleDustMaterial("znfealcl_catalyst", (Zinc.getMaterialRGB()+Iron.getMaterialRGB()+Aluminium.getMaterialRGB()+Chlorine.getMaterialRGB())/4, (short) 77, MaterialIconSet.METALLIC, "ZnFeAlCl");
    public static final Material Difluorobenzophenone = SimpleDustMaterial("difluorobenzophenone", (FluoroBenzene.getMaterialRGB()+Fluorotoluene.getMaterialRGB())/2, (short) 78, MaterialIconSet.SHINY, "(FC6H4)2CO");
    public static final Material AluminiumChloride = SimpleDustMaterial("aluminium_chloride", (Aluminium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 79, MaterialIconSet.SHINY, "AlCl3");
    public static final Material PdFullereneMatrix = SimpleDustMaterial("pd_fullerene_matrix", (Palladium.getMaterialRGB()+Fullerene.getMaterialRGB())/2, (short) 80, MaterialIconSet.SHINY, "PdC73H15NFe");
    public static final Material Terephthalaldehyde = SimpleDustMaterial("terephthalaldehyde", (Dibromomethylbenzene.getMaterialRGB()+SulfuricAcid.getMaterialRGB())/2, (short) 81, MaterialIconSet.FINE, "C8H6O2");
    public static final Material PreZylon = SimpleDustMaterial("pre_zylon", (Terephthalaldehyde.getMaterialRGB()+Dinitrodipropanyloxybenzene.getMaterialRGB())/2, (short) 82, MaterialIconSet.FINE, "C20H22N2O2");
    public static final Material AuPdCCatalyst = SimpleDustMaterial("aupdc_catalyst", (Gold.getMaterialRGB()+Palladium.getMaterialRGB()+Carbon.getMaterialRGB())/3, (short) 83, MaterialIconSet.SHINY, "AuPdC");
    public static final Material Cyanonaphthalene = SimpleDustMaterial("cyanonaphthalene", (SodiumCyanide.getMaterialRGB()+ Naphthalene.getMaterialRGB())/2, (short) 84, MaterialIconSet.FINE, "C11H7N");
    public static final Material TinChloride = SimpleDustMaterial("tin_chloride", (Tin.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 85, MaterialIconSet.ROUGH, "SnCl2");
    public static final Material Triphenylphosphine = SimpleDustMaterial("triphenylphosphine", (Chlorobenzene.getMaterialRGB()+PhosphorusTrichloride.getMaterialRGB())/2, (short) 86, MaterialIconSet.ROUGH, "(C6H5)3P");
    public static final Material Methylbenzophenanthrene = SimpleDustMaterial("methylbenzophenanthrene", (Naphthaldehyde.getMaterialRGB()+Ethylbenzene.getMaterialRGB())/2, (short) 87, MaterialIconSet.FINE, "C19H14");
    public static final Material VanadiumSlag = SimpleDustMaterial("vanadium_slag", (Vanadium.getMaterialRGB()+Titanium.getMaterialRGB())/2, (short) 88, MaterialIconSet.DULL, "(VO)C(TiO2)");
    public static final Material VanadiumSlagDust = SimpleDustMaterial("vanadium_slag_dust", 0xf2ef1b, (short) 89, MaterialIconSet.ROUGH, "VO");
    public static final Material SodiumVanadate = SimpleDustMaterial("sodium_vanadate", 0xf2df1d, (short) 90, MaterialIconSet.ROUGH, "Na3VO4");
    public static final Material AmmoniumVanadate = SimpleDustMaterial("ammonium_vanadate", 0xf2ff1c, (short) 91, MaterialIconSet.FINE, "NH4VO3");
    public static final Material VanadiumOxide = SimpleDustMaterial("vanadium_oxide", 0xf2ef1b, (short) 92, MaterialIconSet.FINE, "V2O5");
    public static final Material BariumCarbonate = SimpleDustMaterial("barium_carbonate", Salt.getMaterialRGB()+10, (short) 94, MaterialIconSet.FINE, "BaCO3");
    public static final Material BariumOxide = SimpleDustMaterial("barium_oxide", (Barium.getMaterialRGB()+Oxygen.getMaterialRGB())/2, (short) 95, MaterialIconSet.FINE, "BaO");
    public static final Material BariumAluminate = SimpleDustMaterial("barium_aluminate", Saltpeter.getMaterialRGB()+10, (short) 96, MaterialIconSet.FINE, "BaAl2O4");
    public static final Material PotassiumUranylTricarbonate = SimpleDustMaterial("potassium_uranyl_carbonate", 0xeff028, (short) 98, MaterialIconSet.METALLIC, "(UO2)CO3");
    public static final Material UraniumPeroxideThoriumOxide = SimpleDustMaterial("uranium_peroxide_thorium_oxide", 0x202020, (short) 99, MaterialIconSet.SHINY, "(UO3)(H2O2)ThO2");
    public static final Material UraniumThoriumOxide = SimpleDustMaterial("uranium_thorium_oxide", 0x202020, (short) 100, MaterialIconSet.SHINY, "UO2ThO2");
    public static final Material UranylThoriumSulfate = SimpleDustMaterial("uranium_thorium_sulfate", 0xe7e848, (short) 101, MaterialIconSet.SHINY, "(UO2)SO4ThO2");
    public static final Material UranylThoriumNitrate = SimpleDustMaterial("uranium_thorium_nitrate", 0xe7e848, (short) 102, MaterialIconSet.SHINY, "UO2(NO3)2Th(NO3)4");
    public static final Material UraniumOxideThoriumNitrate = SimpleDustMaterial("uranium_oxide_thorium_nitrate", 0x33bd45, (short) 103, MaterialIconSet.SHINY, "UO2Th(NO3)4");
    public static final Material CaesiumHydroxide = SimpleDustMaterial("caesium_hydroxide", Caesium.getMaterialRGB()-10, (short) 104, MaterialIconSet.ROUGH, "CsOH");
    public static final Material AluminiumHydroxide = SimpleDustMaterial("aluminium_hydroxide", Aluminium.getMaterialRGB()-25, (short) 105, MaterialIconSet.FINE, "Al(OH)3");
    public static final Material PotassiumCarbonate = SimpleDustMaterial("potassium_carbonate", (Potassium.getMaterialRGB()+Carbon.getMaterialRGB()+Oxygen.getMaterialRGB())/3, (short) 106, MaterialIconSet.FINE, "K2CO3");
    public static final Material GrapheneOxidationResidue = SimpleDustMaterial("graphene_oxidation_residue", 0x96821a, (short) 107, MaterialIconSet.FINE, "(KMnO4)(NaNO3)(H2SO4)");
    public static final Material NiAlOCatalyst = SimpleDustMaterial("nialo_catalyst", 0x0af0af, (short) 108, MaterialIconSet.SHINY, "NiAl2O4");
    public static final Material FeCrOCatalyst = SimpleDustMaterial("fecro_catalyst", 0x8C4517, (short) 109, MaterialIconSet.SHINY, "FeCrO3");
    public static final Material RoastedSpodumene = SimpleDustMaterial("roasted_spodumene", 0x3d3d29, (short) 110, MaterialIconSet.DULL, "LiAlSi2O6");
    public static final Material RoastedLepidolite = SimpleDustMaterial("roasted_lepidolite", 0x470024, (short) 111, MaterialIconSet.DULL, "KLi3Al4O11");
    public static final Material Lithiumthiinediselenide = SimpleDustMaterial("lithiumthiinediselenide", 0x7ada00, (short) 113, MaterialIconSet.METALLIC, "C4H4S2Li2Se2");
    public static final Material NickelChloride = SimpleDustMaterial("nickel_chloride", Nickel.getMaterialRGB()+10, (short) 114, MaterialIconSet.ROUGH, "NiCl2");
    public static final Material PotassiumSulfate = SimpleDustMaterial("potassium_sulfate", (Potassium.getMaterialRGB()+Sulfur.getMaterialRGB())/2, (short) 115, MaterialIconSet.FINE, "K2SO4");
    public static final Material AluminiumSulfate = SimpleDustMaterial("aluminium_sulfate", (Aluminium.getMaterialRGB()+Sulfur.getMaterialRGB())/2, (short) 116, MaterialIconSet.SHINY, "Al2(SO4)3");
    public static final Material BariumHydroxide = SimpleDustMaterial("barium_hydroxide", (Barium.getMaterialRGB()+Hydrogen.getMaterialRGB()+Oxygen.getMaterialRGB())/3, (short) 117, MaterialIconSet.FINE, "Ba(OH)2");
    public static final Material HafniumOxide = SimpleDustMaterial("hafnium_oxide", 0x404040, (short) 118, MaterialIconSet.SHINY, "HfO2");
    public static final Material SiliconChloride = SimpleDustMaterial("silicon_chloride", Silicon.getMaterialRGB()-15, (short) 119, MaterialIconSet.ROUGH, "SiCl4");
    public static final Material HafniumChloride = SimpleDustMaterial("hafnium_chloride", Hafnium.getMaterialRGB()+20, (short) 120, MaterialIconSet.FINE, "HfCl4");
    public static final Material ZincCokePellets = SimpleDustMaterial("zinc_coke_pellets", (Zinc.getMaterialRGB()+Coke.getMaterialRGB())/2, (short) 121, MaterialIconSet.ROUGH, "(H2O)(ZnS)C");
    public static final Material ZincResidualSlag = SimpleDustMaterial("zinc_residual_slag", (Zinc.getMaterialRGB()-20), (short) 122, MaterialIconSet.DULL, "?");
    public static final Material ZincFlueDust = SimpleDustMaterial("zinc_flue_dust", 0xfcfca, (short) 123, MaterialIconSet.ROUGH, "?");
    public static final Material ZincLeachingResidue = SimpleDustMaterial("zinc_leaching_residue", (Germanium.getMaterialRGB()+Oxygen.getMaterialRGB())/2, (short) 124, MaterialIconSet.DULL, "Ge?");
    public static final Material FineZincSlagDust = SimpleDustMaterial("fine_zinc_slag_dust", (Zinc.getMaterialRGB()-10), (short) 125, MaterialIconSet.FINE, "?");
    public static final Material IndiumHydroxide = SimpleDustMaterial("indium_hydroxide", (Indium.getMaterialRGB()+SodiumHydroxide.getMaterialRGB())/2, (short) 126, MaterialIconSet.SHINY, "In(OH)3");
    public static final Material CadmiumZincDust = SimpleDustMaterial("cadmium_zinc_dust", (Cadmium.getMaterialRGB()+Zinc.getMaterialRGB())/2, (short) 127, MaterialIconSet.SHINY, "(H2SO4)CdZn?");
    public static final Material ThalliumResidue = SimpleDustMaterial("thallium_residue", (Thallium.getMaterialRGB()-10), (short) 128, MaterialIconSet.SHINY, "Tl?");
    public static final Material ThalliumChloride = SimpleDustMaterial("thallium_chloride", (Thallium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 129, MaterialIconSet.SHINY, "TlCl");
    public static final Material ZincChloride = SimpleDustMaterial("zinc_chloride", (Zinc.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 130, MaterialIconSet.FINE, "ZnCl2");
    public static final Material SodiumSulfite = SimpleDustMaterial("sodium_sulfite", (SodiumHydroxide.getMaterialRGB()+Sulfur.getMaterialRGB())/2, (short) 131, MaterialIconSet.FINE, "Na2SO3");
    public static final Material Cellulose = SimpleDustMaterial("cellulose", (0xfefefc), (short) 132, MaterialIconSet.DULL, "C6H10O5");
    public static final Material GermaniumOxide = SimpleDustMaterial("germanium_oxide", (Germanium.getMaterialRGB()+10), (short) 133, MaterialIconSet.SHINY, "GeO2");
    public static final Material DisodiumPhosphate = SimpleDustMaterial("sodium_diphosphate", (Sodium.getMaterialRGB()+Phosphorus.getMaterialRGB())/2, (short) 134, MaterialIconSet.ROUGH, "Na2HPO4");
    public static final Material AcrylicFibers = SimpleDustMaterial("acrylic_fibers", 0xfdfdfb, (short) 135, MaterialIconSet.FINE, "(C5O2H8)n");
    public static final Material UranylNitrate = SimpleDustMaterial("uranyl_nitrate", 0x33bd45, (short) 136, MaterialIconSet.SHINY, "U" + "O2" + "(NO2)2");
    public static final Material CalciumSalts = SimpleDustMaterial("calcium_salts", Calcium.getMaterialRGB()-10, (short) 137, MaterialIconSet.ROUGH, of(new MaterialStack(Calcite, 1), new MaterialStack(Gypsum, 1)));
    public static final Material SodiumSalts = SimpleDustMaterial("sodium_salts", Sodium.getMaterialRGB()-5, (short) 138, MaterialIconSet.ROUGH, "NaCl?");
    public static final Material PotassiumMagnesiumSalts = SimpleDustMaterial("kmg_salts", 0xcacac8, (short) 139, MaterialIconSet.ROUGH, "KClMg" + "SO4" + "K2" + "SO4" + "KF");
    public static final Material CalciumMagnesiumSalts = SimpleDustMaterial("camg_salts", 0xcacac8, (short) 140, MaterialIconSet.ROUGH, "Ca" + "CO3" + "(Sr" + "CO3" + ")(CO2)MgO");
    public static final Material SodiumAluminiumHydride = SimpleDustMaterial("sodium_aluminium_hydride", 0x98cafc, (short) 141, MaterialIconSet.ROUGH, "NaAlH4");
    public static final Material LithiumAluminiumHydride = SimpleDustMaterial("lithium_aluminium_hydride", 0xc0defc, (short) 142, MaterialIconSet.ROUGH, "LiAlH4");
    public static final Material SodiumAzanide = SimpleDustMaterial("sodium_azanide", (Sodium.getMaterialRGB()+Hydrogen.getMaterialRGB()+Nitrogen.getMaterialRGB())/3, (short) 143, MaterialIconSet.FINE, "NaNH2");
    public static final Material SodiumAzide = SimpleDustMaterial("sodium_azide", (Sodium.getMaterialRGB()+Nitrogen.getMaterialRGB())/2, (short) 144, MaterialIconSet.FINE, "NaN3");
    public static final Material Glucosamine = SimpleDustMaterial("glucosamine", (Cellulose.getMaterialRGB()+Water.getMaterialRGB())/2, (short) 145, MaterialIconSet.FINE, "C6H13NO5");
    public static final Material AluminiumHydride = SimpleDustMaterial("aluminium_hydride", 0x0b585c, (short) 146, MaterialIconSet.ROUGH, "AlH3");
    public static final Material SodiumHydride = SimpleDustMaterial("sodium_hydride", 0xcacac8, (short) 147, MaterialIconSet.ROUGH, "NaH");
    public static final Material DehydrogenationCatalyst = SimpleDustMaterial("dehydrogenation_catalyst", 0x6464f5, (short) 148, MaterialIconSet.SHINY, "?");
    public static final Material PolystyreneNanoParticles = SimpleDustMaterial("polystryrene_nanoparticles", 0x888079, (short) 149, MaterialIconSet.FINE, "(C8H8)n");
    public static final Material MagnesiumSulfate = SimpleDustMaterial("magnesium_sulfate", 0xcacac8, (short) 150, MaterialIconSet.DULL, "MgSO4");
    public static final Material SodiumMolybdate = SimpleDustMaterial("sodium_molybdate", 0xfcfc00, (short) 151, MaterialIconSet.ROUGH,"Na2MoO4");
    public static final Material SodiumPhosphomolybdate = SimpleDustMaterial("sodium_phosphomolybdate", 0xfcfc00, (short) 152, MaterialIconSet.SHINY, "(MoO3)12Na3PO4");
    public static final Material SodiumPhosphotungstate = SimpleDustMaterial("sodium_phosphotungstate", 0x7a7777, (short) 153, MaterialIconSet.SHINY, "(WO3)12Na3PO4");
    public static final Material Fructose = SimpleDustMaterial("fructose", (Cellulose.getMaterialRGB()+Sugar.getMaterialRGB())/2, (short) 165, MaterialIconSet.ROUGH, "C6H12O6");
    public static final Material Glucose = SimpleDustMaterial("glucose", (Sugar.getMaterialRGB()+5), (short) 166, MaterialIconSet.ROUGH, "C6H12O6");
    public static final Material LeadNitrateCalciumMixture = SimpleDustMaterial("lead_nitrate_calcium_mixture", (LeadNitrate.getMaterialRGB()+Calcium.getMaterialRGB())/2, (short) 167, MaterialIconSet.SHINY, "(Pb(NO3)2)Ca9");
    //FREE ID 168
    public static final Material StrontiumOxide = SimpleDustMaterial("strontium_oxide", 0xcacac8, (short) 169, MaterialIconSet.SHINY, "SrO");
    public static final Material Diiodobiphenyl = SimpleDustMaterial("diiodobiphenyl", 0x000f66, (short) 156, MaterialIconSet.ROUGH, "C12H8I2");
    public static final Material Bipyridine = SimpleDustMaterial("bipyridine", 0X978662, (short) 170, MaterialIconSet.FINE, "C10H8N2");
    public static final Material PalladiumChloride = SimpleDustMaterial("palladium_chloride", 0xb9c0c7, (short) 157, MaterialIconSet.SHINY, "PdCl2");
    public static final Material PalladiumBisDibenzylidieneacetone = SimpleDustMaterial("palladium_bisdibenzylidieneacetone", 0Xbe81a0, (short) 158, MaterialIconSet.ROUGH, "C51H42O3Pd2");
    public static final Material PotassiumTetrachloroplatinate = SimpleDustMaterial("potassium_tetrachloroplatinate", 0xffba54, (short) 159, MaterialIconSet.SHINY, "K2PtCl4");
    public static final Material NickelTriphenylPhosphite = SimpleDustMaterial("nickel_triphenyl_phosphite", 0xd9d973, (short) 160, MaterialIconSet.SHINY, "C36H30Cl2NiP2");
    public static final Material Dichlorocycloctadieneplatinium = SimpleDustMaterial("dichlorocyclooctadieneplatinium", 0xe0f78a, (short) 161, MaterialIconSet.SHINY, "C8H12Cl2Pt");
    public static final Material GrapheneNanotubeMix = SimpleDustMaterial("graphene_nanotube_mix", 0x2c2c2c, (short) 162, MaterialIconSet.ROUGH, "(C)C?");
    public static final Material GrapheneAlignedCNT = SimpleDustMaterial("graphene_aligned_cnt", 0x2c2c2c, (short) 163, MaterialIconSet.SHINY, "(C)C30H20");
    public static final Material NiAlCatalyst = SimpleDustMaterial("nial_catalyst", 0x6ea2ff, (short) 164, MaterialIconSet.SHINY, "NiAl");
    public static final Material TitaniumNitrate = SimpleDustMaterial("titanium_nitrate", 0xFF0066, (short) 171, MaterialIconSet.FINE, "Ti" + "(NO3)4");
    public static final Material SilverNitrate = SimpleDustMaterial("silver_nitrate", 0xfdfdca, (short) 172, MaterialIconSet.SHINY, "AgNO3");
    public static final Material AnodicSlime = SimpleDustMaterial("anodic_slime", 0x765A30, (short) 173, MaterialIconSet.DULL, "SeTe??");
    public static final Material TelluriumOxide = SimpleDustMaterial("tellurium_oxide", 0xFFFF66, (short) 174, MaterialIconSet.SHINY, "TeO2");
    public static final Material SeleniumOxide = SimpleDustMaterial("selenium_oxide", 0xFFFF66, (short) 175, MaterialIconSet.SHINY, "SeO2");
    public static final Material ManganeseSulfate = SimpleDustMaterial("manganese_sulfate", (Manganese.getMaterialRGB()+Sulfur.getMaterialRGB())/2, (short) 176, MaterialIconSet.ROUGH, "MnSO4");
    public static final Material TinSlag = SimpleDustMaterial("tin_slag", 0xc8b9a9, (short) 177, MaterialIconSet.DULL, "NbTa?");
    public static final Material NbTaContainingDust = SimpleDustMaterial("nbta_containing_dust", 0xc8b9a9, (short) 178, MaterialIconSet.ROUGH, "NbTa");
    public static final Material NiobiumTantalumOxide = SimpleDustMaterial("niobium_tantalum_oxide", (Niobium.getMaterialRGB()+Tantalum.getMaterialRGB())/2, (short) 179, MaterialIconSet.SHINY, "(Nb2O5)(Ta2O5)");
    public static final Material FusedColumbite = SimpleDustMaterial("fused_columbite", 0xCCCC00, (short) 180, MaterialIconSet.ROUGH, "(Fe2O3)(NaO)Nb2O5");
    public static final Material LeachedColumbite = SimpleDustMaterial("leached_columbite", 0xCCCC00, (short) 181, MaterialIconSet.SHINY, "(Nb2O5)9Ta2O5?");
    public static final Material FusedTantalite = SimpleDustMaterial("fused_tantalite", 0x915028, (short) 182, MaterialIconSet.ROUGH, "(Fe2O3)(NaO)Ta2O5");
    public static final Material LeachedTantalite = SimpleDustMaterial("leached_tantalite", 0x915028, (short) 183, MaterialIconSet.SHINY, "(Ta2O5)9Nb2O5?");
    public static final Material ColumbiteMinorOxideResidue = SimpleDustMaterial("columbite_minor_oxide_residue", 0x915028, (short) 184, MaterialIconSet.ROUGH, "(BaO)(SnO2)(WO3)(Al2O3)");
    public static final Material TantaliteMinorOxideResidue = SimpleDustMaterial("tantalite_minor_oxide_residue", 0xCCCC00, (short) 185, MaterialIconSet.ROUGH, "(BaO)(ZrO2)(TiO2)(SiO2)");
    public static final Material LeachedPyrochlore = SimpleDustMaterial("leached_pyrochlore", 0x996633, (short) 186, MaterialIconSet.SHINY, "(Nb2O5)9Ta2O5?");
    public static final Material AcidicLeachedPyrochlore = SimpleDustMaterial("acidic_leached_pyrochlore", 0x996633, (short) 187, MaterialIconSet.SHINY, "(H2SO4)Ca12Sr6Ba6?ThUNb26O78F26");
    public static final Material PotasssiumFluoroNiobate = SimpleDustMaterial("potassium_fluoroniobate", 0x73ff00, (short) 188, MaterialIconSet.SHINY, "K2NbF7");
    public static final Material PotasssiumFluoroTantalate = SimpleDustMaterial("potassium_fluorotantalate", 0x73ff00, (short) 189, MaterialIconSet.SHINY, "K2TaF7");
    public static final Material BariumPeroxide = SimpleDustMaterial("barium_peroxide", (Barium.getMaterialRGB()+Oxygen.getMaterialRGB()-30)/2, (short) 190, MaterialIconSet.SHINY, "BaO2");
    public static final Material CassiteriteCokePellets = SimpleDustMaterial("cassiterite_coke_pellets", 0x8f8f8f, (short) 191, MaterialIconSet.ROUGH, "SnO2C?");
    public static final Material IronSulfateDust = SimpleDustMaterial("iron_sulfate_dust", (Iron.getMaterialRGB()+Sulfur.getMaterialRGB())/2, (short) 192, MaterialIconSet.SHINY, "FeSO4");
    public static final Material StrontiumCarbonate = SimpleDustMaterial("strontium_carbonate", 0xcacac8, (short) 193, MaterialIconSet.ROUGH, "Sr" + "CO4");
    public static final Material SodiumHypochlorite = SimpleDustMaterial("sodium_hypochlorite", 0x6cff50, (short) 195, MaterialIconSet.SHINY, "NaClO");
    public static final Material DehydratedLignite = SimpleDustMaterial("dehydrated_lignite", 0x5c4020, (short) 196, MaterialIconSet.DULL, "C2(H2O)4C?");
    public static final Material BCEPellet = SimpleDustMaterial("bce_pellet", 0x3c3020, (short) 197, MaterialIconSet.LIGNITE, "C2(H2O)4C");
    public static final Material CopperGalliumIndiumMix = SimpleDustMaterial("copper_gallium_indium_mix", (Indium.getMaterialRGB() + Copper.getMaterialRGB() + Gallium.getMaterialRGB()) / 3, (short) 198, MaterialIconSet.ROUGH, "CuGaIn");
    public static final Material CopperGalliumIndiumSelenide = SimpleDustMaterial("copper_gallium_indium_selenide", (CopperGalliumIndiumMix.getMaterialRGB() + Selenium.getMaterialRGB()) / 2, (short) 199, MaterialIconSet.SHINY, "CuGaInSe2");
    public static final Material LanthanumCalciumManganate = SimpleDustMaterial("lanthanum_gallium_manganate", 0x8aa07b, (short) 200, MaterialIconSet.SHINY, "LaCaMnO3");
    public static final Material AluminiumComplex = SimpleDustMaterial("aluminium_complex", 0x3f5a9f, (short) 201, MaterialIconSet.SHINY, "AlC9H7NO");
    public static final Material IronPlatinumCatalyst = SimpleDustMaterial("iron_platinum_catalyst", Iron.getMaterialRGB() / 2 + Platinum.getMaterialRGB() / 2, (short) 205, MaterialIconSet.SHINY, "FePt");
    public static final Material YttriumNitrate = SimpleDustMaterial("yttrium_nitrate", 0xdadafc, (short) 206, MaterialIconSet.SHINY, "Y(NO3)3");
    public static final Material CopperNitrate = SimpleDustMaterial("copper_nitrate", 0xcaecec, (short) 254, MaterialIconSet.SHINY, "Cu(NO3)2");
    public static final Material BariumNitrate = SimpleDustMaterial("barium_nitrate", (Barium.getMaterialRGB()+NitricAcid.getMaterialRGB())/2, (short) 207, MaterialIconSet.SHINY, "Ba(NO3)2");
    public static final Material WellMixedYBCOxides = SimpleDustMaterial("well_mixed_ybc_oxides", 0x2c3429, (short) 208, MaterialIconSet.ROUGH, "YBa2Cu3O6");
    public static final Material PiledTBCC = SimpleDustMaterial("piled_tbcc", 0x669900, (short) 209, MaterialIconSet.SHINY, "Tl2Ba2Cu3Ca2");
    public static final Material ActiniumOxalate = SimpleDustMaterial("actinium_oxalate", Actinium.getMaterialRGB(), (short) 210, MaterialIconSet.SHINY, "Ac(CO2)4");
    public static final Material ActiniumHydride = SimpleDustMaterial("actinium_hydride", Actinium.getMaterialRGB(), (short) 211, MaterialIconSet.SHINY, "AcH3");
    public static final Material LanthanumFullereneMix = SimpleDustMaterial("lanthanum_fullerene_mix", 0xdfcafa, (short) 212, MaterialIconSet.SHINY, "(C60)2La2?");
    public static final Material LanthanumEmbeddedFullerene = SimpleDustMaterial("lanthanum_embedded_fullerene", 0x99cc00, (short) 213, MaterialIconSet.SHINY, "(C60)2La2");
    public static final Material IronIodide = SimpleDustMaterial("iron_iodide", (Iron.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 214, MaterialIconSet.ROUGH, "FeI2");
    public static final Material PotassiumIodide = SimpleDustMaterial("potassium_iodide", (Potassium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 215, MaterialIconSet.ROUGH, "KI");
    public static final Material ThalliumIodide = SimpleDustMaterial("thallium_iodide", (Thallium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 216, MaterialIconSet.ROUGH, "TlI");
    public static final Material ScandiumIodide = SimpleDustMaterial("scandium_iodide", (Scandium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 217, MaterialIconSet.ROUGH, "ScI3");
    public static final Material RubidiumIodide = SimpleDustMaterial("rubidium_iodide", (Rubidium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 218, MaterialIconSet.ROUGH, "RbI");
    public static final Material IndiumIodide = SimpleDustMaterial("indium_iodide", (Indium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 219, MaterialIconSet.ROUGH, "InI3");
    public static final Material GalliumIodide = SimpleDustMaterial("gallium_iodide", (Gallium.getMaterialRGB()+Iodine.getMaterialRGB())/2, (short) 220, MaterialIconSet.ROUGH, "GaI3");
    public static final Material UVAHalideMix = SimpleDustMaterial("uva_halide_mix", (GalliumIodide.getMaterialRGB()+PotassiumIodide.getMaterialRGB()+Mercury.getMaterialRGB())/3, (short) 221, MaterialIconSet.SHINY, "Hg(GaI3)KI");
    public static final Material WhiteHalideMix = SimpleDustMaterial("white_halide_mix", (ScandiumIodide.getMaterialRGB()+PotassiumIodide.getMaterialRGB()+Mercury.getMaterialRGB())/3, (short) 222, MaterialIconSet.SHINY, "Hg(ScI3)KI");
    public static final Material BlueHalideMix = SimpleDustMaterial("blue_halide_mix", (IndiumIodide.getMaterialRGB()+PotassiumIodide.getMaterialRGB()+Mercury.getMaterialRGB())/3, (short) 223, MaterialIconSet.SHINY, "Hg(InI3)KI");
    public static final Material GreenHalideMix = SimpleDustMaterial("green_halide_mix", (ThalliumIodide.getMaterialRGB()+PotassiumIodide.getMaterialRGB()+Mercury.getMaterialRGB())/3, (short) 224, MaterialIconSet.SHINY, "Hg(TlI)KI");
    public static final Material RedHalideMix = SimpleDustMaterial("red_halide_mix", (RubidiumIodide.getMaterialRGB()+PotassiumIodide.getMaterialRGB()+Mercury.getMaterialRGB())/3, (short) 225, MaterialIconSet.SHINY, "Hg(RbI)KI");
    public static final Material CarbonylPurifiedIron = SimpleDustMaterial("carbonyl_purified_iron", Iron.getMaterialRGB(), (short) 226, MaterialIconSet.SHINY, "Fe");
    public static final Material BariumTriflate = SimpleDustMaterial("barium_triflate", (Barium.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 227, MaterialIconSet.SHINY, "Ba(OSO2CF3)2");
    public static final Material ScandiumTriflate = SimpleDustMaterial("scandium_triflate", 0xdfcfcf, (short) 228, MaterialIconSet.SHINY, "Sc(OSO2CF3)3");
    public static final Material SodiumThiosulfate = SimpleDustMaterial("sodium_thiosulfate", 0x2090fc, (short) 229, MaterialIconSet.FINE, "Na2S2O3");
    public static final Material TitaniumCyclopentadienyl = SimpleDustMaterial("titanium_cyclopentadienyl", 0xbc30bc, (short) 230, MaterialIconSet.SHINY, "(C5H5)2Cl2Ti");
    public static final Material SodiumBromide = SimpleDustMaterial("sodium_bromide", 0xfeaffc, (short) 231, MaterialIconSet.ROUGH, "NaBr");
    public static final Material FranciumCarbide = SimpleDustMaterial("francium_carbide", Francium.getMaterialRGB(), (short) 232, MaterialIconSet.SHINY, "Fr2C2");
    public static final Material BoronCarbide = SimpleDustMaterial("boron_carbide", 0x303030, (short) 233, MaterialIconSet.FINE, "B4C3");
    public static final Material BoronFranciumCarbide = SimpleDustMaterial("boron_francium_carbide", 0x808080, (short) 234, MaterialIconSet.SHINY, "Fr4B4C7");
    public static final Material MixedAstatideSalts = SimpleDustMaterial("mixed_astatide_salts", 0x6df63f, (short) 235, MaterialIconSet.SHINY, "(At3)(Ho)(Th)(Cn)(Fl)");
    public static final Material SodiumIodide = SimpleDustMaterial("sodium_iodide", 0x555588, (short) 236, MaterialIconSet.ROUGH, "NaI");
    public static final Material SodiumIodate = SimpleDustMaterial("sodium_iodate", 0x11116d, (short) 237, MaterialIconSet.ROUGH, "NaIO3");
    public static final Material SodiumPeriodate = SimpleDustMaterial("sodium_periodate", 0x11116d, (short) 238, MaterialIconSet.SHINY, "NaIO4");
    public static final Material SodiumSeaborgate = SimpleDustMaterial("sodium_seaborgate", 0x55bbd4, (short) 239, MaterialIconSet.SHINY, "Na2SgO4");
    public static final Material StrontiumChloride = SimpleDustMaterial("strontium_chloride", 0x3a9aba, (short) 241, MaterialIconSet.SHINY, "SrCl2");
    public static final Material YttriumEuropiumVanadate = SimpleDustMaterial("yttrium_europium_vanadate", (Yttrium.getMaterialRGB()+ Europium.getMaterialRGB()+Vanadium.getMaterialRGB())/3, (short) 242, MaterialIconSet.SHINY, "YEuVO4");
    public static final Material StrontiumEuropiumAluminate = SimpleDustMaterial("strontium_europium_aluminate", (Strontium.getMaterialRGB()+ Europium.getMaterialRGB()+Aluminium.getMaterialRGB())/3, (short) 243, MaterialIconSet.SHINY, "SrEuAl2O4");
    public static final Material BariumStrontiumTitanate = SimpleDustMaterial("barium_strontium_titanate", 0xFF0066, (short) 244, MaterialIconSet.SHINY, "BaO4SrTi");
    public static final Material PotassiumManganate = SimpleDustMaterial("potassium_manganate", 0xaf20af, (short) 246, MaterialIconSet.DULL, "K2MnO4");
    public static final Material BariumChloride = SimpleDustMaterial("barium_chloride", (Barium.getMaterialRGB()+Chlorine.getMaterialRGB())/2, (short) 247, MaterialIconSet.SHINY, "BaCl2");
    public static final Material TantalumOxide = SimpleDustMaterial("tantalum_oxide", (Tantalum.getMaterialRGB()+Oxygen.getMaterialRGB())/2, (short) 248, MaterialIconSet.SHINY, "Ta2O5");
    public static final Material ZirconylChloride = SimpleDustMaterial("zirconyl_chloride", ZirconiumTetrachloride.getMaterialRGB(), (short) 249, MaterialIconSet.SHINY, "ZrOCl2");
    public static final Material LeadSenenide = SimpleDustMaterial("lead_selenide", (Lead.getMaterialRGB()+Selenium.getMaterialRGB())/2, (short) 250, MaterialIconSet.ROUGH, "PbSe");
    public static final Material LeadScandiumTantalate = SimpleDustMaterial("lead_scandium_tantalate", (Lead.getMaterialRGB()+Scandium.getMaterialRGB()+Tantalum.getMaterialRGB())/3, (short) 251, MaterialIconSet.SHINY, "Pb(ScTa)O3");
    public static final Material BETS = SimpleDustMaterial("bets", 0x7ada00, (short) 253, MaterialIconSet.SHINY, "C10H8S4Se4");
    public static final Material MagnetorestrictiveAlloy = SimpleDustMaterial("magnetorestrictive_alloy", 0xafefef, (short) 252, MaterialIconSet.ROUGH, "Tb4Dy7Fe10Co5B2SiC");
    public static final Material BoronOxide = SimpleDustMaterial("boron_oxide",(Boron.getMaterialRGB()+Oxygen.getMaterialRGB())/2,(short) 256,MaterialIconSet.ROUGH, "B2O3");
    public static final Material LithiumAluminiumFluoride = SimpleDustMaterial("lithium_aluminium_fluoride",(Lithium.getMaterialRGB()+Aluminium.getMaterialRGB()+Fluorine.getMaterialRGB())/3,(short) 263,MaterialIconSet.ROUGH, "AlF4Li");
    public static final Material HafniumCarbide = SimpleDustMaterial("hafnium_carbide",0x2c2c2c,(short) 264,MaterialIconSet.SHINY, "HfC");
    public static final Material SeaborgiumCarbide = SimpleDustMaterial("seaborgium_carbide",0x2c2c2c,(short) 266,MaterialIconSet.SHINY, "SgC");
    public static final Material AluminiumNitrate = SimpleDustMaterial("aluminium_nitrate",Alumina.getMaterialRGB(),(short) 267,MaterialIconSet.SHINY, "Al(NO3)3");
    public static final Material NeodymiumDopedYttrium = SimpleDustMaterial("neodymium_doped_yttrium",YttriumOxide.getMaterialRGB(),(short) 268,MaterialIconSet.DULL, "Nd:Y?");
    public static final Material NdYAGNanoparticles = SimpleDustMaterial("nd_yag_nanoparticles",0x6f20af,(short) 269,MaterialIconSet.SHINY, "Nd:YAG");
    public static final Material PotassiumPermanganate = SimpleDustMaterial("potassium_permanganate",PotassiumManganate.getMaterialRGB()-15,(short) 270,MaterialIconSet.ROUGH, "KMnO4");
    public static final Material Urea = SimpleDustMaterial("urea",0x30cf20,(short) 271,MaterialIconSet.ROUGH, "CH4N2O");
    //FREE IDs 272-274
    public static final Material FluoroapatiteSolidResidue = SimpleDustMaterial("fluoroapatite_solid_residue",0x3cb290,(short) 275,MaterialIconSet.SHINY, "Ca6PO4SiO3F");
    public static final Material ApatiteSolidResidue = SimpleDustMaterial("apatite_solid_residue",0x3cb290,(short) 319,MaterialIconSet.FINE, "Ca6PO4SiO3");
    public static final Material AmmoniumBifluoride = SimpleDustMaterial("ammonium_bifluoride",0x20cfcf,(short) 276,MaterialIconSet.ROUGH, "NH4HF2");
    public static final Material SodiumArsenate = SimpleDustMaterial("sodium_arsenate",0xbffabf,(short) 277,MaterialIconSet.METALLIC, "Na3AsO4");
    public static final Material PrHoYLFNanoparticles = SimpleDustMaterial("prho_ylf_nanoparticles",0xcf8acf,(short) 278,MaterialIconSet.SHINY, "Pr/Ho:YLF");
    public static final Material LuTmYVONanoparticles = SimpleDustMaterial("lutm_yvo_nanoparticles",0x206faf,(short) 279,MaterialIconSet.SHINY, "Lu/Tm:YVO");
    public static final Material PureSodiumVanadate = SimpleDustMaterial("pure_sodium_vanadate", SodiumVanadate.getMaterialRGB(), (short) 280,MaterialIconSet.SHINY, "Na3VO4");
    public static final Material AmmoniumCarbonate = SimpleDustMaterial("ammonium_carbonate", AmmoniumSulfate.getMaterialRGB(), (short) 281,MaterialIconSet.DULL, "(NH4)2CO3");
    public static final Material CadmiumSulfide = SimpleDustMaterial("cadmium_sulfide",0xffff00,(short) 282,MaterialIconSet.ROUGH, "CdS");
    public static final Material UnprocessedNdYAGDust = SimpleDustMaterial("unprocessed_ndyag_dust",0x6f20af,(short) 283,MaterialIconSet.DULL, "Nd:YAG?");
    public static final Material LuTmYVOPrecipitate = SimpleDustMaterial("lutm_yvo_precipitate",0xcf8acf,(short) 318,MaterialIconSet.DULL, "Lu/Tm:YVO?");
    public static final Material RawSienna = SimpleDustMaterial("raw_siena",0x663300,(short) 284,MaterialIconSet.ROUGH, "SiO2(MnO2)(FeO2)");
    public static final Material BurnedSienna = SimpleDustMaterial("burned_siena",0xff0000,(short) 285,MaterialIconSet.DULL, "SiO2(MnO2)(FeO2)");
    public static final Material BismuthVanadate = SimpleDustMaterial("bismuth_vanadate",0xffff00,(short) 286,MaterialIconSet.SHINY, "BiVO4");
    public static final Material CopperArsenite = SimpleDustMaterial("copper_arsenite",0x66ff66,(short) 287,MaterialIconSet.ROUGH, "Cu3(AsO4)2");
    public static final Material MercuryIodide = SimpleDustMaterial("mercury_iodide",0xff0000,(short) 288,MaterialIconSet.DULL, "HgI2");
    public static final Material TitaniumYellow = SimpleDustMaterial("titanium_yellow",0xffff00,(short) 289,MaterialIconSet.SHINY, "NiO(Sb2O3)(TiO2)20");
    public static final Material CobaltZincOxide = SimpleDustMaterial("cobalt_zinc_oxide",0x00ffff,(short) 290,MaterialIconSet.SHINY, "CoZn4O5");
    public static final Material ScheelesGreen = SimpleDustMaterial("scheeles_green",0x00ff00,(short) 291,MaterialIconSet.DULL, "AsCuHO3");
    public static final Material CobaltAluminate = SimpleDustMaterial("cobalt_aluminate",0x0000ff,(short) 292,MaterialIconSet.FINE, "Al2Co2O5");
    public static final Material PotassiumFerrocyanide = SimpleDustMaterial("potassium_ferrocyanide",0x0000ff,(short) 293,MaterialIconSet.ROUGH, "K4Fe(CN)6(H2O)3");
    public static final Material PrussianBlue = SimpleDustMaterial("prussian_blue",0x0000ff,(short) 294,MaterialIconSet.DULL, "Fe4[Fe(CN)6]3");
    public static final Material AmmoniumManganesePhosphate = SimpleDustMaterial("ammonium_manganese_phosphate",0x660066,(short) 295,MaterialIconSet.SHINY, "NH4MnPO4");
    public static final Material HanPurple = SimpleDustMaterial("hans_purple",0x660066,(short) 296,MaterialIconSet.DULL, "BaCuSi2O6");
    public static final Material ChromeYellow = SimpleDustMaterial("chrome_yellow",0xffff00,(short) 297,MaterialIconSet.DULL, "PbCrO4");
    public static final Material ChromeOrange = SimpleDustMaterial("chrome_orange",0xff6600,(short) 298,MaterialIconSet.DULL, "Pb2CrO5");
    public static final Material DiaminostilbenedisulfonicAcid = SimpleDustMaterial("diaminostilbenedisulfonic_acid",0xffffff,(short) 299,MaterialIconSet.DULL, "C14H14N2O6S2");
    public static final Material Nigrosin = SimpleDustMaterial("nigrosin",0x000000,(short) 300,MaterialIconSet.DULL, "C36H26N5ClNa2S2O6");
    public static final Material DirectBrown = SimpleDustMaterial("direct_brown",0x663300,(short) 301,MaterialIconSet.DULL, "C26H19N6NaO3S");
    public static final Material DianilineterephthalicAcid = SimpleDustMaterial("dianilineterephthalic_acid",0xff0000,(short) 302,MaterialIconSet.DULL, "C20H16N2O4");
    public static final Material Quinacridone = SimpleDustMaterial("quinacridone",0xff0000,(short) 303,MaterialIconSet.DULL, "C20H12N2O2");
    public static final Material DiarylideYellow = SimpleDustMaterial("diarylide_yellow",0xffff00,(short) 304,MaterialIconSet.DULL, "C32H26Cl2N6O4");
    public static final Material AlizarineCyanineGreen = SimpleDustMaterial("alizarine_cyanine_green",0x00ff00,(short) 305,MaterialIconSet.DULL, "C28H20N2Na2O8S2");
    public static final Material Aminoanthraquinone = SimpleDustMaterial("aminoanthraquinone",0x0000ff,(short) 306,MaterialIconSet.DULL, "C14H9NO2");
    public static final Material IndanthroneBlue = SimpleDustMaterial("indanthrone_blue",0x0000ff,(short) 307,MaterialIconSet.DULL, "C28H14N2O2");
    public static final Material Diketopyrrolopyrrole = SimpleDustMaterial("diketopyrrolopyrrole",0xff6600,(short) 308,MaterialIconSet.DULL, "C18H12N2O2");
    public static final Material Mauveine = SimpleDustMaterial("mauveine",0x660066,(short) 309,MaterialIconSet.DULL, "C26H23N4");
    public static final Material Indigo = SimpleDustMaterial("indigo",0x0000ff,(short) 310,MaterialIconSet.DULL, "C16H10N2O2");
    public static final Material Tetrabromoindigo = SimpleDustMaterial("tetrabromoindigo",0x00ff00,(short) 311,MaterialIconSet.DULL, "C16H6Br2N2O2");
    public static final Material CyanIndigoDye = SimpleDustMaterial("cyan_indigo_dye",0x009999,(short) 312,MaterialIconSet.DULL, "(C16H10N2O2)2Br2");
    public static final Material Fluorescein = SimpleDustMaterial("fluorescein",0x990000,(short) 313,MaterialIconSet.DULL, "C20H12O5");
    public static final Material Erythrosine = SimpleDustMaterial("erythrosine",0xff00ff,(short) 314,MaterialIconSet.DULL, "C20H6I4Na2O5");
    public static final Material ManganeseIIIOxide = SimpleDustMaterial("manganese_iii_oxide", Pyrolusite.getMaterialRGB(), (short) 315, MaterialIconSet.DULL, "Mn2O3");
    public static final Material MercuryChloride = SimpleDustMaterial("mercury_chloride",0xd6b8ad,(short) 316,MaterialIconSet.ROUGH, "HgCl2");
    public static final Material SodiumSulfanilate = SimpleDustMaterial("sodium_sulfanilate",0xe49879,(short) 317,MaterialIconSet.SHINY, "C6H6NNaO3S");
    public static final Material Anthraquinone = SimpleDustMaterial("anthraquinone",0xfff782,(short) 320,MaterialIconSet.ROUGH, "C14H8O2");
    public static final Material LithiumHydride = SimpleDustMaterial("lithium_hydride",(Lithium.getMaterialRGB()+Hydrogen.getMaterialRGB())/2,(short) 321,MaterialIconSet.ROUGH, "LiH");
    public static final Material NiobiumChloride = SimpleDustMaterial("niobium_chloride",Niobium.getMaterialRGB(),(short) 322, MaterialIconSet.SHINY, "NbCl5");
    public static final Material NiobiumHydroxide = SimpleDustMaterial("niobium_hydroxide",0x7c7c7c,(short) 323,MaterialIconSet.ROUGH, "Nb(OH)5");
    public static final Material MagnesiumFluoride = SimpleDustMaterial("magnesium_fluoride",0xcfcfcf,(short) 324,MaterialIconSet.SHINY, "MgF2");
    public static final Material LithiumNiobateNanoparticles = SimpleDustMaterial("lithium_niobate_nanoparticles",LithiumNiobate.getMaterialRGB()-10,(short) 326,MaterialIconSet.SHINY, "LiNbO4");
    public static final Material LithiumHydroxide = SimpleDustMaterial("lithium_hydroxide", (Lithium.getMaterialRGB()+Oxygen.getMaterialRGB()+Hydrogen.getMaterialRGB())/3, (short) 327, MaterialIconSet.DULL, "LiOH");
    public static final Material RhReNqCatalyst = SimpleDustMaterial("rhrenq_catalyst",(Rhenium.getMaterialRGB()+Rhodium.getMaterialRGB()+Naquadah.getMaterialRGB())/3,(short) 328, MaterialIconSet.SHINY, "ReRhNq");
    public static final Material PalladiumAcetate = SimpleDustMaterial("palladium_acetate",0xcc3300,(short) 329,MaterialIconSet.SHINY, "C4H6O4Pd");
    public static final Material FranciumCaesiumCadmiumBromide = SimpleDustMaterial("francium_caesium_cadmium_bromide",(Francium.getMaterialRGB()+Caesium.getMaterialRGB()+Cadmium.getMaterialRGB()+Bromine.getMaterialRGB())/4,(short) 330,MaterialIconSet.SHINY, "FrCsCf2Br6");
    public static final Material ZincSelenide = SimpleDustMaterial("zinc_selenide",0xfcfc00,(short) 331,MaterialIconSet.FINE, "ZnSe");
    public static final Material RhodamineB = SimpleDustMaterial("rhodamine_b",0xfc2020,(short) 332,MaterialIconSet.SHINY, "C28H31ClN2O3");
    public static final Material Stilbene = SimpleDustMaterial("stilbene",0x3c9c3c,(short) 333,MaterialIconSet.SHINY, "C14H12");
    public static final Material Tetracene = SimpleDustMaterial("tetracene",0x99801a,(short) 334,MaterialIconSet.SHINY, "C18H12");
    public static final Material DitertbutylDicarbonate = SimpleDustMaterial("ditertbutyl_dicarbonate",0xccccf6,(short) 335, MaterialIconSet.ROUGH, "C10H18O5");
    public static final Material PotassiumBromide = SimpleDustMaterial("potassium_bromide",0xe066a3,(short) 336,MaterialIconSet.FINE, "KBr");
    public static final Material PotassiumBromate = SimpleDustMaterial("potassium_bromate",0x8a4cd1,(short) 337,MaterialIconSet.ROUGH, "KBrO3");
    public static final Material IBX = SimpleDustMaterial("ibx",0x20208c,(short) 338,MaterialIconSet.SHINY, "C7H5IO4");
    public static final Material SodiumPertechnetate = SimpleDustMaterial("sodium_pertechnetate",0x6162c4,(short) 339,MaterialIconSet.SHINY, "NaTcO4");
    public static final Material PotassiumPertechnate = SimpleDustMaterial("potassium_pertechnate",0xdec451,(short) 340,MaterialIconSet.SHINY, "KTcO4");
    public static final Material PotassiumPerrhenate = SimpleDustMaterial("potassium_perrhenate",0xdec451,(short) 341,MaterialIconSet.SHINY, "KReO4");
    public static final Material PotassiumNonahydridotechnetate = SimpleDustMaterial("potassium_nonahydridotechnetate",0xede2a4,(short) 342,MaterialIconSet.SHINY, "H9K2TcO4");
    public static final Material PotassiumNonahydridorhenate = SimpleDustMaterial("potassium_nonahydridorhenate",0xeae2a8,(short) 343,MaterialIconSet.SHINY, "H9K2ReO4");
    public static final Material LithiumIodide = SimpleDustMaterial("lithium_iodide",(Lithium.getMaterialRGB()+Iodine.getMaterialRGB()),(short) 344,MaterialIconSet.ROUGH, "LiI");
    public static final Material PalladiumLoadedRutileNanoparticles = SimpleDustMaterial("palladium_loaded_rutile_nanoparticles",(Palladium.getMaterialRGB()+Rutile.getMaterialRGB()),(short) 345,MaterialIconSet.FINE, "PdTiO2");
    public static final Material SaccharicAcid = SimpleDustMaterial("saccharic_acid",Glucose.getMaterialRGB(),(short) 346,MaterialIconSet.METALLIC, "C6H10O8");
    public static final Material AdipicAcid = SimpleDustMaterial("adipic_acid",0xda9288,(short) 347,MaterialIconSet.ROUGH, "C6H10O4");
    public static final Material TetraethylammoniumNonahydridides = SimpleDustMaterial("tetraethylammonium_nonahydrides",0xbee8b9,(short) 348,MaterialIconSet.SHINY, "(C8H20N)(ReH9)(TcH9)");
    public static final Material ManganeseFluoride = SimpleDustMaterial("manganese_fluoride",Pyrolusite.getMaterialRGB(),(short) 349,MaterialIconSet.ROUGH, "MnF2");
    public static final Material GermaniumSulfide = SimpleDustMaterial("germanium_sulfide",GermaniumOxide.getMaterialRGB(),(short) 350,MaterialIconSet.ROUGH, "GeS2");
    public static final Material BismuthGermanate = SimpleDustMaterial("bismuth_germanate",0x94cf5c,(short) 351,MaterialIconSet.ROUGH, "Bi12GeO20");
    public static final Material CesiumIodide = SimpleDustMaterial("cesium_iodide",CaesiumHydroxide.getMaterialRGB(),(short) 352, MaterialIconSet.SHINY, "CsI");
    public static final Material TlTmCesiumIodide = SimpleDustMaterial("tl_tm_cesium_iodide",CaesiumHydroxide.getMaterialRGB()*9/10+Thallium.getMaterialRGB()/10,(short) 353,MaterialIconSet.SHINY, "CsITlTm");
    public static final Material CadmiumTungstate = SimpleDustMaterial("cadmium_tungstate",CalciumTungstate.getMaterialRGB(),(short) 354,MaterialIconSet.SHINY, "CdWO4");
    public static final Material PolycyclicAromaticMix = SimpleDustMaterial("polycyclic_aromatic_mix",Tetracene.getMaterialRGB(),(short) 355,MaterialIconSet.ROUGH, "C18H12");
    public static final Material SodiumOxide = SimpleDustMaterial("sodium_oxide", 0x0373fc, (short) 356, MaterialIconSet.SHINY, "Na2O");
    public static final Material GrapheneOxide = SimpleDustMaterial("graphene_oxide",Graphene.getMaterialRGB(),(short) 357,MaterialIconSet.ROUGH, "C(O2)");
    public static final Material GraphiteOxide = SimpleDustMaterial("graphite_oxide", Graphite.getMaterialRGB(), (short) 358,MaterialIconSet.FINE, "C(O2)");
    public static final Material ChromiumIIIOxide = SimpleDustMaterial("chromium_iii_oxide",0x4bf25f,(short) 359,MaterialIconSet.ROUGH,"Cr2O3");
    public static final Material GrapheneGelSuspension = SimpleDustMaterial("graphene_gel_suspension",0xadadad,(short) 360,MaterialIconSet.ROUGH, "C");
    public static final Material DryGrapheneGel = SimpleDustMaterial("dry_graphene_gel",0x3a3ada,(short) 361,MaterialIconSet.DULL, "C");
    public static final Material SodiumPerchlorate = SimpleDustMaterial("sodium_perchlorate", Salt.getMaterialRGB(),(short) 376, MaterialIconSet.SHINY, "NaClO4");
    public static final Material Lanthanoids = SimpleDustMaterial("lanthanoids",(Lanthanum.getMaterialRGB()+Cerium.getMaterialRGB()+Praseodymium.getMaterialRGB()+Neodymium.getMaterialRGB()+Promethium.getMaterialRGB()+Samarium.getMaterialRGB()+Europium.getMaterialRGB()+Gadolinium.getMaterialRGB()+Terbium.getMaterialRGB()+Dysprosium.getMaterialRGB()+Holmium.getMaterialRGB()+Erbium.getMaterialRGB()+Thulium.getMaterialRGB()+Ytterbium.getMaterialRGB()+Lutetium.getMaterialRGB())/15,(short) 362,MaterialIconSet.SHINY, "LaPrNdPmSmEuGdTbDyHoErTmYbLu");
    public static final Material Actinoids = SimpleDustMaterial("actinoids",(Actinium.getMaterialRGB()+Thorium.getMaterialRGB()+Protactinium.getMaterialRGB()+ Uranium238.getMaterialRGB()+Neptunium.getMaterialRGB()+ Plutonium239.getMaterialRGB()+Americium.getMaterialRGB()+Curium.getMaterialRGB()+Berkelium.getMaterialRGB()+Californium.getMaterialRGB()+Einsteinium.getMaterialRGB()+Fermium.getMaterialRGB()+Mendelevium.getMaterialRGB())/13,(short) 363,MaterialIconSet.SHINY, "AcThPaNpPuAmCmBkCfEsFmMd");
    public static final Material Alkalis = SimpleDustMaterial("alkalis",(Lithium.getMaterialRGB()+Beryllium.getMaterialRGB()+Sodium.getMaterialRGB()+Magnesium.getMaterialRGB()+Potassium.getMaterialRGB()+Calcium.getMaterialRGB()+Scandium.getMaterialRGB()+Rubidium.getMaterialRGB()+Strontium.getMaterialRGB()+Yttrium.getMaterialRGB()+Caesium.getMaterialRGB()+Barium.getMaterialRGB()+Francium.getMaterialRGB()+Radium.getMaterialRGB())/12, (short) 364, MaterialIconSet.SHINY, "LiBeNaMgKCaScRbSrYCeBaFrRa");
    public static final Material PreciousMetals = SimpleDustMaterial("precious_metals",(Ruthenium.getMaterialRGB()+Rhodium.getMaterialRGB()+Palladium.getMaterialRGB()+Silver.getMaterialRGB()+Rhenium.getMaterialRGB()+Osmium.getMaterialRGB()+Iridium.getMaterialRGB()+Platinum.getMaterialRGB()+Gold.getMaterialRGB())/9, (short) 365, MaterialIconSet.SHINY, "RuRhPdAgReOsIrPtAu");
    public static final Material LightTranstionMetals = SimpleDustMaterial("light_transition_metals",(Titanium.getMaterialRGB()+Vanadium.getMaterialRGB()+Chromium.getMaterialRGB()+Manganese.getMaterialRGB()+Iron.getMaterialRGB()+Cobalt.getMaterialRGB()+Nickel.getMaterialRGB()+Copper.getMaterialRGB())/8,(short) 366, MaterialIconSet.SHINY, "TiVCrMnFeCoNiCu");
    public static final Material RefractoryMetals = SimpleDustMaterial("refractory_metals",(Zirconium.getMaterialRGB()+ Niobium.getMaterialRGB()+Molybdenum.getMaterialRGB()+Technetium.getMaterialRGB()+Hafnium.getMaterialRGB()+Tantalum.getMaterialRGB()+Tungsten.getMaterialRGB())/7, (short) 367, MaterialIconSet.SHINY, "ZrNbMoTcHfTaW");
    public static final Material PostTransitionMetals = SimpleDustMaterial("post_transition_metals",(Aluminium.getMaterialRGB()+Silicon.getMaterialRGB()+Zinc.getMaterialRGB()+Gallium.getMaterialRGB()+Germanium.getMaterialRGB()+Cadmium.getMaterialRGB()+Indium.getMaterialRGB()+Tin.getMaterialRGB()+Antimony.getMaterialRGB()+Mercury.getMaterialRGB()+Thallium.getMaterialRGB()+Lead.getMaterialRGB()+Bismuth.getMaterialRGB()+Polonium.getMaterialRGB())/14, (short) 368, MaterialIconSet.SHINY, "AlSiZnGaGeCdInSnSbHgTlPbBiPo");
    public static final Material MercuryAcetate = SimpleDustMaterial("mercury_acetate",0xcc8562, (short) 369, MaterialIconSet.ROUGH, "Hg(CH3COO)2");
    public static final Material CalciumCyanamide = SimpleDustMaterial("calcium_cyanamide", CalciumCarbide.getMaterialRGB(), (short) 370, MaterialIconSet.SHINY, "CaCN2");
    public static final Material SelectivelyMutatedCupriavidiusNecator = SimpleDustMaterial("selectively_mutated_cupriavidius_necator", CupriavidusNecator.getMaterialRGB() * 5 / 4, (short) 373, MaterialIconSet.SHINY, "Bacteria");
    public static final Material PurifiedColumbite = SimpleDustMaterial("purified_columbite", LeachedColumbite.getMaterialRGB(), (short) 371, MaterialIconSet.SHINY, "Ta2O5Nb18O45");
    public static final Material PurifiedPyrochlore = SimpleDustMaterial("purified_pyrochlore", LeachedPyrochlore.getMaterialRGB(), (short) 372, MaterialIconSet.SHINY, "Ta2O5Nb18O45");
    public static final Material ChargedCesiumCeriumCobaltIndium = SimpleDustMaterial("charged_cesium_cerium_cobalt_indium", 0x52ad25, (short) 380, MaterialIconSet.SHINY, "CsCeCo2In10");
    public static final Material RheniumChloride = SimpleDustMaterial("rhenium_chloride", 0x3c2a5c, (short) 381, MaterialIconSet.SHINY, "ReCl5");
    public static final Material AntimonyTrichloride = SimpleDustMaterial("antimony_trichloride", AntimonyTrifluoride.getMaterialRGB(), (short) 382, MaterialIconSet.SHINY, "SbCl3");
    public static final Material LithiumCobaltOxide = SimpleDustMaterial("lithium_cobalt_oxide",0xd2a4f3,(short) 387, MaterialIconSet.SHINY, "LiCoO");
    public static final Material LithiumTriflate = SimpleDustMaterial("lithium_triflate",0xe2dae3, (short) 388, MaterialIconSet.SHINY, "LiCSO3F3");
    public static final Material Xylose = SimpleDustMaterial("xylose", Glucose.getMaterialRGB(),(short) 389, MaterialIconSet.ROUGH, "C5H10O5");
    public static final Material SiliconNanoparticles = SimpleDustMaterial("silicon_nanoparticles",Silicon.getMaterialRGB(),(short) 390, MaterialIconSet.SHINY, "Si?");
    public static final Material Halloysite = SimpleDustMaterial("halloysite", 0x23423a, (short) 391, MaterialIconSet.ROUGH, "Al9Si10O50Ga");
    public static final Material GalliumChloride = SimpleDustMaterial("gallium_chloride", 0x92867a, (short) 392, MaterialIconSet.ROUGH, "GaCl3");
    public static final Material SulfurCoatedHalloysite = SimpleDustMaterial("sulfur_coated_halloysite", 0x23973a, (short) 393, MaterialIconSet.ROUGH, "S2C2(Al9Si10O50Ga)");
    public static final Material FluorideBatteryElectrolyte = SimpleDustMaterial("fluoride_battery_electrolyte", 0x9a628a, (short) 394, MaterialIconSet.SHINY, "La9BaF29(C8H7F)");
    public static final Material LanthanumNickelOxide = SimpleDustMaterial("lanthanum_nickel_oxide", Garnierite.getMaterialRGB()/2+Lanthanum.getMaterialRGB()/2, (short) 395, MaterialIconSet.SHINY, "La2NiO4");
    public static final Material Sorbose = SimpleDustMaterial("sorbose", Glucose.getMaterialRGB(), (short) 396, MaterialIconSet.DULL, "C6H12O6");
    public static final Material CalciumAlginate = SimpleDustMaterial("calcium_alginate",0x654321, (short) 397, MaterialIconSet.ROUGH, "CaC12H14O12");
    public static final Material NickelOxideHydroxide = SimpleDustMaterial("nickel_oxide_hydroxide",0xa2f2a2,(short) 398, MaterialIconSet.METALLIC, "NiO(OH)");
    public static final Material BETSPerrhenate = SimpleDustMaterial("bets_perrhenate", 0x7ada00, (short) 255, MaterialIconSet.SHINY, "ReC10H8S4Se4O4");
    public static final Material TBCCODust = SimpleDustMaterial("tbcco_dust", 0x669900, (short) 257, MaterialIconSet.SHINY, "TlBa2Ca2Cu3O10");
    public static final Material BorocarbideDust = SimpleDustMaterial("borocarbide_dust", 0x9a9a2a, (short) 258, MaterialIconSet.SHINY, "B4C7Fr4At6Ho2Th2Fl2Cn2");
    public static final Material ActiniumSuperhydride = SimpleDustMaterial("actinium_superhydride", Actinium.getMaterialRGB() * 9 / 8, (short) 259, MaterialIconSet.SHINY, "AcH12");
    public static final Material StrontiumSuperconductorDust = SimpleDustMaterial("strontium_superconductor_dust", 0x45abf4, (short) 260, MaterialIconSet.SHINY, "Sr2RuSgO8");
    public static final Material FullereneSuperconductiveDust = SimpleDustMaterial("fullerene_superconductor_dust", 0x99cc00, (short) 261, MaterialIconSet.SHINY, "LaCsRb(C60)2");
    public static final Material HassiumChloride = SimpleDustMaterial("hassium_chloride", 0x5d2abc, (short) 383, MaterialIconSet.SHINY, "HsCl4");
    public static final Material RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexafluorophosphate = SimpleDustMaterial("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea", 0xa26a8b,(short) 384, MaterialIconSet.SHINY, "ReHsTlC60PN12H84S6O12F6");
    public static final Material Legendarium = SimpleDustMaterial("legendarium", 0xffffff, (short) 385, MaterialIconSet.SHINY, "NqNq+*Nq*DrTrKeTnAdVb");
    public static final Material LanthanumFullereneNanotubes = SimpleDustMaterial("lanthanum_fullerene_nanotubes", LanthanumFullereneMix.getMaterialRGB()*3/5, (short) 386, MaterialIconSet.SHINY, "La2(C60)2CNT");
    public static final Material SodiumMetavanadate = SimpleDustMaterial("sodium_metavanadate", SodaAsh.getMaterialRGB(),(short) 262, MaterialIconSet.FINE, "NaVO3");
    public static final Material PotassiumPeroxymonosulfate = SimpleDustMaterial("potassium_peroxymonosulfate", (PotassiumMetabisulfite.getMaterialRGB() + 20), (short) 374, MaterialIconSet.FINE, "KHSO5");
    public static final Material CoAcABCatalyst = SimpleDustMaterial("coacab_catalyst", 0x755f30, (short) 900, MaterialIconSet.FINE, "Co/AC-AB");
    public static final Material SilverPerchlorate = SimpleDustMaterial("silver_perchlorate", SilverChloride.getMaterialRGB(), (short) 902, MaterialIconSet.SHINY, "AgClO4");
    public static final Material SodiumChlorate = SimpleDustMaterial("sodium_chlorate", Salt.getMaterialRGB(), (short) 901, MaterialIconSet.ROUGH, "NaClO3");
    public static final Material CopperLeach = SimpleDustMaterial("copper_leach", 0x765A30, (short) 903, MaterialIconSet.SHINY, "Cu3?");
    public static final Material GoldLeach = SimpleDustMaterial("gold_leach", 0xBBA52B, (short) 904, MaterialIconSet.ROUGH, "Cu3Au?");
    public static final Material Durene = SimpleDustMaterial("durene", 0xA39C95, (short) 905, MaterialIconSet.ROUGH, "C6H2(CH3)4");
    public static final Material PyromelliticDianhydride = SimpleDustMaterial("pyromellitic_dianhydride", 0xF0EAD6, (short) 906, MaterialIconSet.SHINY, "C6H2(C2O3)2");
    public static final Material CaliforniumTrioxide = SimpleDustMaterial("californiumtrioxide", 0x7cc922, (short) 907, MaterialIconSet.ROUGH, "Cf2O3");
    public static final Material CaliforniumTrichloride = SimpleDustMaterial("californiumtrichloride", 0x3e9837, (short) 908, MaterialIconSet.ROUGH, "CfCl3");
    public static final Material IridiumTrioxide = SimpleDustMaterial("iridiumtrioxide", 0x9a9a2b, (short) 909, MaterialIconSet.ROUGH,"Ir2O3");
    public static final Material PotassiumHydroxylaminedisulfonate = SimpleDustMaterial("potassium_hydroxylaminedisulfonate", (0xF0EAD6 + NitrousAcid.getMaterialRGB())/2, (short) 910, MaterialIconSet.DULL, "KHSO3");
    public static final Material SuccinicAnhydride = SimpleDustMaterial("succinic_anhydride", (SuccinicAcid.getMaterialRGB() + AceticAnhydride.getMaterialRGB())/2, (short) 911, MaterialIconSet.DULL, "(CH2CO)2O");
    public static final Material AmmoniumAcetate = SimpleDustMaterial("ammonium_acetate", 0xb6dee0, (short) 912, MaterialIconSet.DULL, "NH4CH3CO2");
    public static final Material Acetamide = SimpleDustMaterial("acetamide", 0xa6bebf, (short) 913, MaterialIconSet.DULL, "CH3CONH2");
    public static final Material Acetonitrile = SimpleDustMaterial("acetonitrile", 0xa2afb0, (short) 914, MaterialIconSet.DULL, "CH3CN");
    public static final Material NHydroxysuccinimide = SimpleDustMaterial("n-hydroxysuccinimide", 0xdbcae3, (short) 915, MaterialIconSet.DULL, "(CH2CO)2NOH");
    public static final Material Hexabenzylhexaazaisowurtzitane = SimpleDustMaterial("hexabenzylhexaazaisowurtzitane", 0x624573, (short) 916, MaterialIconSet.DULL, "C48N6H48");
    public static final Material SuccinimidylAcetate = SimpleDustMaterial("succinimidyl_acetate", 0xbd93a6, (short) 917, MaterialIconSet.DULL, "C6H7NO4");
    public static final Material DibenzylTetraacetylhexaazaisowurtzitane = SimpleDustMaterial("dibenzyltetraacetylhexaazaisowurtzitane", 0xb3c98b, (short) 918, MaterialIconSet.DULL, "C28N6H32O4");
    public static final Material HexanitroHexaazaisowurtzitane = SimpleDustMaterial("hexanitrohexaazaisowurtzitane", 0x414a4f, (short) 919, MaterialIconSet.SHINY, "C6H6N12O12");
    public static final Material NitroniumTetrafluoroborate = SimpleDustMaterial("nitronium_tetrafluoroborate", 0x686c6e, (short) 920, MaterialIconSet.DULL, "NO2BF4");
    public static final Material NitrosoniumTetrafluoroborate = SimpleDustMaterial("nitrosonium_tetrafluoroborate", 0x7e8d94, (short) 921, MaterialIconSet.DULL, "NOBF4");
    public static final Material Hexamethylenetetramine = SimpleDustMaterial("hexamethylenetetramine", 0x7e8d94, (short) 922, MaterialIconSet.DULL, "(CH2)6N4");
    public static final Material PdCCatalyst = SimpleDustMaterial("pdc_catalyst", (Palladium.getMaterialRGB() + Carbon.getMaterialRGB())/2, (short) 923, MaterialIconSet.DULL, "PdC");
    public static final Material Tetraacetyldinitrosohexaazaisowurtzitane = SimpleDustMaterial("tetraacetyldinitrosohexaazaisowurtzitane",(DibenzylTetraacetylhexaazaisowurtzitane.getMaterialRGB()+Hexabenzylhexaazaisowurtzitane.getMaterialRGB())/2,(short) 924, MaterialIconSet.DULL, "C14N8H18O6");
    public static final Material CrudeHexanitroHexaazaisowurtzitane = SimpleDustMaterial("crude_hexanitrohexaazaisowurtzitane", HexanitroHexaazaisowurtzitane.getMaterialRGB()*5/7, (short) 925, MaterialIconSet.DULL, "C6H6N12O12");
    public static final Material PotassiumBisulfite = SimpleDustMaterial("potassium_bisulfite", 0xF0EAD6, (short) 926, MaterialIconSet.DULL, "KHSO3");
    public static final Material PotassiumNitrite = SimpleDustMaterial("potassium_nitrite", 0xF0EAD6, (short) 927, MaterialIconSet.DULL, "KNO2");
    public static final Material HydroxylammoniumSulfate = SimpleDustMaterial("hydroxylammonium_sulfate", 0xF0EAD6, (short) 928, MaterialIconSet.DULL, "(NH3OH)2SO4");
    public static final Material SodiumBorohydride = SimpleDustMaterial("sodium_borohydride", 0xc2c2fa, (short) 929, MaterialIconSet.ROUGH, "NaBH4");
    public static final Material SodiumTetrafluoroborate = SimpleDustMaterial("sodium_tetrafluoroborate", (SodiumBorohydride.getMaterialRGB()+Fluorine.getMaterialRGB())/2, (short) 930, MaterialIconSet.DULL, "NaBF4");
    public static final Material Decaborane = SimpleDustMaterial("decaborane", Diborane.getMaterialRGB(), (short) 931, MaterialIconSet.DULL, "B10H14");
    public static final Material CesiumCarboranePrecusor = SimpleDustMaterial("cesium_carborane_precursor", 0xb56487, (short) 932, MaterialIconSet.ROUGH, "CsB10H12CN(CH3)3Cl");
    public static final Material CesiumCarborane = SimpleDustMaterial("cesium_carborane", CesiumCarboranePrecusor.getMaterialRGB()*6/5, (short) 933, MaterialIconSet.DULL, "CsCB11H12");
    public static final Material Fluorocarborane = SimpleDustMaterial("fluorocarborane", 0x20EB7A, (short) 934, MaterialIconSet.SHINY, "HCHB11F11");
    public static final Material CaesiumNitrate = SimpleDustMaterial("caesium_nitrate", 0x7452DC, (short) 935, MaterialIconSet.ROUGH, "CsNO3");
    public static final Material SilverIodide = SimpleDustMaterial("silver_iodide", (SilverChloride.getMaterialRGB()*2+Iodine.getMaterialRGB())/3, (short) 936, MaterialIconSet.ROUGH, "AgI");
    public static final Material ActiniumTriniumHydroxides = SimpleDustMaterial("actinium_trinium_hydroxides", (ActiniumOxalate.getMaterialRGB()+Trinium.getMaterialRGB())/2, (short) 937, MaterialIconSet.ROUGH, "Ke3Ac2(OH)12");
    public static final Material TriniumTetrafluoride = SimpleDustMaterial("trinium_tetrafluoride", 0x477347, (short) 938, MaterialIconSet.SHINY, "KeF4");
    public static final Material ActiniumNitrate = SimpleDustMaterial("actinium_nitrate", YttriumNitrate.getMaterialRGB(), (short) 939, MaterialIconSet.ROUGH, "Ac(NO3)3");
    public static final Material RadiumNitrate = SimpleDustMaterial("radium_nitrate", BariumNitrate.getMaterialRGB(), (short) 940, MaterialIconSet.DULL, "Ra(NO3)2");
    public static final Material PhosphorousPentasulfide = SimpleDustMaterial("phosphorous_pentasulfide", 0xEBAD24, (short) 941, MaterialIconSet.DULL, "P4S10");
    public static final Material PureCrystallineNitricAcid = SimpleDustMaterial("crystalline_nitric_acid", NitricAcid.getMaterialRGB(), (short) 942, MaterialIconSet.ROUGH, "HNO3");
    public static final Material StoneResidueDust = SimpleDustMaterial("stone_residue_dust", Stone.getMaterialRGB()/5*3, (short) 944, MaterialIconSet.ROUGH);
    public static final Material DiamagneticResidues = SimpleDustMaterial("diamagnetic_residues", (Calcium.getMaterialRGB()+Zinc.getMaterialRGB()+Copper.getMaterialRGB()+Gallium.getMaterialRGB()+Beryllium.getMaterialRGB()+Tin.getMaterialRGB())/15, (short) 945, MaterialIconSet.DULL);
    public static final Material HeavyDiamagneticResidues = SimpleDustMaterial("heavy_diamagnetic_residues", (Lead.getMaterialRGB()+Mercury.getMaterialRGB()+Cadmium.getMaterialRGB()+Indium.getMaterialRGB()+Gold.getMaterialRGB()+Bismuth.getMaterialRGB())/15, (short) 946, MaterialIconSet.DULL);
    public static final Material ParamagneticResidues = SimpleDustMaterial("paramagnetic_residues", (Sodium.getMaterialRGB()+Potassium.getMaterialRGB()+Magnesium.getMaterialRGB()+Titanium.getMaterialRGB()+Vanadium.getMaterialRGB()+Manganese.getMaterialRGB())/15, (short) 947, MaterialIconSet.DULL);
    public static final Material HeavyParamagneticResidues = SimpleDustMaterial("heavy_paramagnetic_residues", (Thorium.getMaterialRGB()+Thallium.getMaterialRGB()+Uranium235.getMaterialRGB()+Tungsten.getMaterialRGB()+Hafnium.getMaterialRGB()+Tantalum.getMaterialRGB())/15, (short) 948, MaterialIconSet.DULL);
    public static final Material FerromagneticResidues = SimpleDustMaterial("ferromagnetic_residues", (Iron.getMaterialRGB()+Nickel.getMaterialRGB()+Cobalt.getMaterialRGB())/7, (short) 949, MaterialIconSet.DULL);
    public static final Material HeavyFerromagneticResidues = SimpleDustMaterial("heavy_ferromagnetic_residues", DysprosiumOxide.getMaterialRGB()*3/11, (short) 950, MaterialIconSet.DULL);
    public static final Material UncommonResidues = SimpleDustMaterial("uncommon_residues", (Triniite.getMaterialRGB()+NaquadriaticTaranium.getMaterialRGB()+PreciousMetals.getMaterialRGB())/5, (short) 951, MaterialIconSet.FINE);
    public static final Material PartiallyOxidizedResidues = SimpleDustMaterial("partially_oxidized_residues", StoneResidueDust.getMaterialRGB()+Dioxygendifluoride.getMaterialRGB(), (short) 952, MaterialIconSet.DULL);
    public static final Material MetallicResidues = SimpleDustMaterial("metallic_residues", (DiamagneticResidues.getMaterialRGB()+ParamagneticResidues.getMaterialRGB()+ FerromagneticResidues.getMaterialRGB()+UncommonResidues.getMaterialRGB()/3)/4, (short) 953 ,MaterialIconSet.DULL);
    public static final Material HeavyMetallicResidues = SimpleDustMaterial("heavy_metallic_residues", (HeavyDiamagneticResidues.getMaterialRGB()+HeavyParamagneticResidues.getMaterialRGB()+HeavyFerromagneticResidues.getMaterialRGB()+UncommonResidues.getMaterialRGB()/3)/4, (short) 954 ,MaterialIconSet.DULL);
    public static final Material OxidizedResidues = SimpleDustMaterial("oxidized_residues", (DiamagneticResidues.getMaterialRGB()+ParamagneticResidues.getMaterialRGB()+ FerromagneticResidues.getMaterialRGB()+0x9f0000)/4, (short) 955, MaterialIconSet.DULL);
    public static final Material HeavyOxidizedResidues = SimpleDustMaterial("heavy_oxidized_residues", (DiamagneticResidues.getMaterialRGB()+ParamagneticResidues.getMaterialRGB()+ FerromagneticResidues.getMaterialRGB()+0x9f0000)/4, (short) 956, MaterialIconSet.DULL);
    public static final Material ExoticHeavyResidues = SimpleDustMaterial("exotic_heavy_residues",NitratedTriniiteSolution.getMaterialRGB(),(short) 957, MaterialIconSet.SHINY);
    public static final Material CleanInertResidues = SimpleDustMaterial("clean_inert_residues", (Taranium.getMaterialRGB()+Xenon.getMaterialRGB())/2, (short) 958, MaterialIconSet.SHINY);
    public static final Material IridiumCyclooctadienylChlorideDimer = SimpleDustMaterial("iridium_cyclooctadienyl_chloride_dimer", (Dichlorocycloctadieneplatinium.getMaterialRGB()+Iridium.getMaterialRGB())/2, (short) 943, MaterialIconSet.SHINY, "Ir2(C8H12)2Cl2");
    public static final Material FinelyPowderedRutile = SimpleDustMaterial("finely_powdered_rutile", 0xffffff, (short) 959, MaterialIconSet.FINE, "TiO2");
    public static final Material InertResidues = SimpleDustMaterial("inert_residues", 0x61587a, (short) 960, MaterialIconSet.SHINY);

    public static Material SamariumMagnetic;
    public static Material Quantum;




    public static void init() {
        /*Zirconium = new Builder(id("zirconium"))
                .ingot()
                .color(0xE0E1E1)
                .iconSet(MaterialIconSet.METALLIC)
                .element(GTElements.Zr)
                .buildAndRegister();*/


        GoldChain.init();
        LithiumChain.init();
    }

    public static void modifyMaterials() {

        Material[] ingotinize = new Material[]{
                GTMaterials.Zirconium,
                GTMaterials.Copernicium,
                GTMaterials.Protactinium,
                GTMaterials.Neptunium,
                GTMaterials.Curium,
                GTMaterials.Berkelium,
                GTMaterials.Californium,
                GTMaterials.Einsteinium,
                GTMaterials.Fermium,
                GTMaterials.Mendelevium,
                GTMaterials.Francium,
                GTMaterials.Radium,
                GTMaterials.Actinium,
                GTMaterials.Hafnium,
                GTMaterials.Rhenium,
                GTMaterials.Technetium,
                GTMaterials.Thallium,
                GTMaterials.Germanium,
                GTMaterials.Selenium,
                GTMaterials.Bromine,
                GTMaterials.Iodine,
                GTMaterials.Astatine,
                GTMaterials.Rutherfordium,
                GTMaterials.Dubnium,
                GTMaterials.Seaborgium,
                GTMaterials.Bohrium,
                GTMaterials.Tennessine,
                GTMaterials.Livermorium,
                GTMaterials.Moscovium,
                GTMaterials.Nihonium,
                GTMaterials.Roentgenium,
                GTMaterials.Meitnerium
        };

        for (Material material : ingotinize) {
            material.setProperty(PropertyKey.INGOT, new IngotProperty());
        }

        GTMaterials.Protactinium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Neptunium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Curium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Berkelium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Californium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Einsteinium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Fermium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Mendelevium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Thallium.addFlags(MaterialFlags.GENERATE_FOIL);
        GTMaterials.Germanium.addFlags(MaterialFlags.GENERATE_PLATE);
        GTMaterials.Seaborgium.addFlags(MaterialFlags.GENERATE_FRAME);
        GTMaterials.Bohrium.addFlags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_ROUND);
    }

}
