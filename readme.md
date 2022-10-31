[![Java Build and Artifact Upload](https://github.com/Hempflingclub/Immortality-Fabric/actions/workflows/gradle.yml/badge.svg)](https://github.com/Hempflingclub/Immortality-Fabric/actions/workflows/gradle.yml)

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/immortality-fabric) 

[Requires Fabric API ![](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Fabric_API.png?raw=true)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

[Requires Cardinal Components ![](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Cardinal_Components.png?raw=true)](https://www.curseforge.com/minecraft/mc-mods/cardinal-components)
# Immortality  Mod Features
- Achieve Different Kinds of Immortality
- Complete the Quest of Immortality

![](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Quest_of_Immortality.png?raw=true)
- Strengthen your Immortality and reach for True Immortality
- For Debug Information you can use the command
  - /immortality:stats 
- To begin your Quest kill the Ender Dragon

![](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Killing_EnderDragon.png?raw=true)
  - It will drop a Void Heart on these Deaths 

| Deaths |
|--------|
| 1      |
| 2      |
| 4      |
| 6      |
| ...    |
- Consume the Void Heart and need less sustenance, or craft Items to progress on your Quest
- Most Items have clear Descriptions so reading ahead may Spoil your Fun of exploring the Mod
- As an Immortal you can extract your Liver of Immortality with the Holy Dagger

![](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/src/main/resources/assets/immortality/textures/item/holy_dagger.png?raw=true)
- The Liver of Immortality when eaten will grant False Immortality
  - The Liver is gained by using a Holy Dagger as an Immortal / True Immortal or also by crafting
  - And Also by using Bane Of Life Enchantment (explanation in its section)
- The Life Elixir will give 1 Heart by default every 25%, any Immorality has a 50% chance, except True Immortality as for them it's guaranteed
- You can reach Semi Immortality as an False Immortal, by successfully consuming 10 Life Elixirs
- A Semi Immortal, will still lose Hearts as an False Immortal, but on Death simply respawn with normal Hearts, also slowly regain Hearts
- Use the Summoning Sigil to summon the Immortal Wither (a very hard Boss) it will drop a Heart of Immortality
- Soul Binding
  - Every Type Of Immortal can Shift Right Click with the Holy Dagger onto an Entity which has been Named (not Players) and bind their Immortality to them
  - If they die, the Immortal will die aswell. On the Immortals Final Death, the Entity will likewise die
  - Soul Binding will only work when the Player is online, otherwise the Entity is mortal as by default
- Bane Of Life Enchantment
  - Use the Bane Of Life Enchantment, to steal an Immortals Liver upon killing them
  - Killing Immortals/True Immortals 3 times in quick succession will make them temporarily Semi Immortals (killable)
  - When Killing False or Semi Immortals (Final Kill), they will drop a Life Elixir
  - When Killing an SoulBound Entity, their SoulBond gets Broken, and they die
  - Normal Players will get Weakness and Slowness corresponding to the Enchantment Level
- Crafting Recipes

![Holy Dagger](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Recipe_Holy_Dagger.png?raw=true)
![Immortal Essence](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Recipe_Immortal_Essence.png?raw=true)
![Liver of Immortality](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Recipe_Liver_of_Immortality.png?raw=true)
![Summoning Sigil](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Recipe_Summoning_Sigil.png?raw=true)
![Life Elixir](https://github.com/Hempflingclub/Immortality-Fabric/blob/master/.github/readme/Recipe_Life_Elixir.gif?raw=true)
-------
# TODO

## Artists
 |Textures|Models|Planned|
 |:-:|:-:|:-:|
 |[` `]|[` `]|Heart of Immortality|
 |[` `]|[` `]|Void Heart|
 |[` `]|[` `]|Liver of Immortality|
 |[` `]|[` `]|Holy Dagger|
 |[` `]|[` `]|True Immortality|
 |[` `]|[` `]|Bane Of Life|
 |[` `]|[` `]|Immortal Wither|
 |[`X`]|[`X`]|Current Holy Dagger|

---------------------------
# Devs
|Textures|Models|Planned|
 |:-:|:-:|:-:|
 |[`X`]|[`X`]|Life Elixir|
 |[`X`]|[`X`]|Current Textures except Holy Dagger|
### Planned
- [x] Immortality "Steal" Enchantment to force harvest immortal Liver
- [x] Heart/Liver of Immortality on Entitys not being Players (SoulBinding)
- [x] True Immortality needs 3 Extracted Livers, and only then will you also be able to eat your own Liver to regrow it
- [x] Life Elixir, which gives 1 Heart every 10%, but with True Immortality it is guaranteed
- [x] Immortal Souls
- [x] Bonus to Immortal Liver (Semi Immortality(Lose Hearts but never lose Semi Immortality, and slowly regain Hearts))
- [x] Immortal Wither, killing him will drop the Heart of Immortality
- [ ] Waiting for Feedback to decide other Features which should be added
---
## Version 2.0.0 (WIP)
Thanks to driplock's suggestions on CurseForge I've come up with these following changes I plan on implementing for 2.0.0 after fixing existing Bugs (if everything goes as planned, otherwise it will be adjusted accordingly)
  - ### New Item Soul Stone
    - [ ] If held in Offhand when killing will absorb Soul Power
    - [ ] It shows it's Soul Power in Tooltip
    - [ ] Can hold up to 5 Soul Power
    - [ ] Mobs give different amounts of Soul Power scaling based of Health
      - Will not work on Players because of their Immutable Souls 
    - Has 3 Durability of emptying it until it breaks
    - [ ] Can Empty it's Soul Power into your SoulBound
    - [ ] When Dropped in Powdered Snow will condense into 1/5 it's Soul Power's Immortal Essence
      - If this happens below 5 Soul Power it will be nulled
      - This Condesation uses Durability
  - ### SoulBound integration with Soul Power mechanic
    - [ ] SoulBounds will have some form of Indicator regarding their SoulPower
    - [ ] They can hold a full Soul Stone charge (5) of Soul Power max regardless of the SoulBounds Entity Type
    - [ ] Should a Semi Immortal now have a FINAL Death their SoulBound sacrifices 1 Soul Power to avoid it
    - [ ] Should a Semi Immortal have a FINAL Death without their SoulBound having at least 1 Soul Power they will die and so will their SoulBound
    - [ ] SoulBound's will be restricted to False Immortals having consumed enough Life Elixir to ready themselves for Semi Immortality and upwards in terms of Progression
    - [ ] Semi Immortals will be False Immortals without a SoulBound (No Heart restoration every 5min)
    - [ ] SoulBounds can be sacrificed by Immortals to reach the newly changed True Immortality for (Soul Power Sacrificed + Entity Value) in minutes
      - [ ] This new True Immortality will also be invulnerable to Bane Of Life
      - [ ] Will give glowing / Strength 3 / Speed 2 / Creative Flight
      - [ ] Entity Value will range from 1-5 depending on their sacrificed worth
      - [ ] After the Effect the Player will Permanently lose 2 Hearts, regardless if they choose to give up their Immortality afterwards
      - [ ] After the Effect they will also be forcefully Mortal for 10 Minutes regardless of True Immortality Time achieved by the formula
    - [ ] Any Type of Immortal can kill their own SoulBound without needing Bane Of Life, so beware
    - [ ] For Every Immortal Type SoulBound's will use 1 Soul Power to save themselves from a Bane Of Life Death and then be force summoned to you, unless you're Offline
  - [ ] Rework Existing True Immortality to be a Strong Immortal, and also give clear feedback regarding radiating Immortality healing Items (already in 1.0.0+) and add a toggle Command for it (maybe someone doesn't like their blinking pickaxe when mining)