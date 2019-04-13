# O aplikaci

Tato aplikace je počítačovým zpracováním známé hry *Racetrack* s rozhraním pro jednoduchou implementaci vlastního AI.

Hra Racetrack se většinou hraje s tužkou a papírem v několika lidech. Každý hráč má jedno autíčko a cílem hry je projet předem namalovanou závodní dráhu za co nejméně tahů. Každé autíčko má svůj vektor pohybu, který znázorňuje, kterým směrem a jak rychle se autíčko pohybuje. Každé kolo je nová pozice autíčka určena tím, že je k jeho pozici přičten jeho vektor pohybu. Hráči autíčka ovládají tím, že každé kolo mohou obě složky (složka rovnoběžná s x-osou a složka rovnoběžná s y-osou) vektoru pohybu svého autíčka zvětšit nebo zmenšit o 1, nebo ponechat stejné.

SCREENSHOT-VEKTORY

Aplikace umožňuje:

 - Hrát hru Racetrack na různých mapách v libovolném počtu hráčů.

 - Hrát proti AI protivníkům.

 - Nechat proti sobě hrát pouze AI hráče.

 - Přidat do hry vlastní AI pomocí jednoduchého rozhraní.

 - S pomocí Tiled map editoru jednoduše vytvořit další mapy do hry.

# Intalace a spuštění

Pro spuštění aplikace není nutná instalace. Stačí si stáhnout soubor Racetrack_v2.jar a spustit ho. Tento soubor lze stáhnout zde: [github.com/Sheld5/Racetrack_v2/tree/master/out/artifacts/Racetrack_v2_jar](https://github.com/Sheld5/Racetrack_v2/tree/master/out/artifacts/Racetrack_v2_jar)

Pro fungování aplikace je nutné pouze mít nainstalovanou aktuální verzi softwaru Java (JRE 12). Java lze stáhnout zde: [www.java.com/en/download](https://www.java.com/en/download/)

Pro vytváření vlastních map do hry je potřeba si stáhnout a nainstalovat editor map "Tiled". Ten lze stáhnout zdarma na této adrese: [www.mapeditor.org](https://www.mapeditor.org/)

# Používání aplikace

## Menu

Po spuštění aplikace se otevře okno s hlavním menu. Zde jsou tlačítka "Play" a "Exit". Pokud chceme hrát hru, klikneme na tlačítko "Play". Tlačítkem "Exit" můžeme aplikaci vypnout.

SCREENSHOT-MAIN-MENU

Po stisknutí tlačítka "Play" se dostaneme do nastavení hry. Zde si můžeme pomocí grafického uživatelského rozhraní zvolit veškerá nastavení hry, než hru zapneme.

V horní části okna aplikace můžeme pomocí rozbalovacího seznamu vybrat mapu, na které chceme hrát.

Uprostřed se nachází velký šedý obdélník pro nastavení hráčů. V něm je mimo jiné tlačítko "Add Player". Tímto tlačítkem můžeme přidávat hráče. Když ho stiskneme, uvidíme, že je do obdélníku přidán nový tmavě šedý pruh, obsahující další různá nastavení, stejný jako ten, co tam byl od začátku. Každý z těchto pruhů znázorňuje jednoho hráče, který bude ve hře závodit, a obsahuje veškerá nastavení pro tohoto hráče. První (zleva) je tlačítko, kterým můžeme vybírat barvu auta pro daného hráče. Klikáním na toto tlačítko rotujeme mezi možnými barvami. Druhé je textové pole, ve kterém si můžeme zvolit jméno hráče. Třetí je rozbalovací seznam, pomocí kterého můžeme nastavit, jestli má být tento hráč ovládán počítačem. Pokud chceme, aby byl tento hráč ovládán člověkem, zvolíme možnost "HUMAN". Pokud chceme, aby byl ovládán jednou z umělých inteligencí, zvolíme příslušnou z nich v seznamu. Nakonec se zde nachází tlačítko s červeným křížkem, pomocí kterého můžeme hráče odebrat.

Ve spodní části obrazovky se nachází dvě tlačítka. Tlačítkem "Start Game" spustíme hru s nastavením, které je momentálně zvoleno. Stisknutím tlačítka "Back" se vrátíme zpět do hlavního menu.

SCREENSHOT-SETTINGS

## Ovládání hry

Po spuštění hry se nám zobrazí mapa s různými políčky a všechna auta na startu. Přes políčka kolem aut jsou zobrazeny světle zelené čtverečky s puntíky uprostřed, které dohromady tvoří větší čtverec 3x3 políčka velký. Tento čtverec budeme odteď nazývat "crosshair". Crosshair slouží k tomu, aby hráči mohli ovládat svá autíčka. Crosshair je vždy vykreslen tak, aby jeho prostřední políčko bylo to, kam autíčko hráče, který je na tahu, dojede, pokud jeho vektor pohybu hráč toto kolo nezmění. Hráč ale může každé kolo změnit obě složky pohybového vektoru svého autíčka o +-1 nebo je ponechat stejné. Z toho vyplývá, že má každé kolo přesně 9 možností svého dalšího tahu. Políčka, na kterých jeho autíčko skončí po odehrání tahu jsou znázorněny jednotlivými políčky crosshair. Hráč tedy své autíčko ovládá tak, že vybere políčko crosshair, které odpovídá políčku, na které chce své autíčko přesunout.

Pokud je na tahu lidksý hráč, při přejíždění kurzorem myši přes crosshair, se jeho políčka, nad kterými se zrovna kurzor nachází, červeně zvýrazňují. Svůj výběr potvrdí hráč kliknutím.

Pokud je na tahu AI (počítačem řízený) hráč, červeně se zvýrazní políčko, které odpovídá tahu, který chce AI provést. Uživatel pak musí stisknout klávesu ENTER a tím je tah proveden.

Pomocí tlačítek PLUS a MINUS můžeme zvětšovat a zmenšovat mapu, podle toho, jak se nám to hodí.

Co dělá které tlačítko, si můžeme připomenout ve spodní části okna pod mapou, kde je ovládání stručně popsáno. Napravo od mapy můžeme v průběhu hry sledovat kolikátý tah právě provádíme, jelikož je zde znázorněn žlutým číslem. Pod ním je tlačítko "Back", pomocí kterého se můžeme dostat zpět do nastavení hry (tím je ale ztracen stav hry a při příštím spuštění začne hra zase od začátku).

Po dokončení závodu se zobrazí okno s výsledky jednotlivých hráčů. Zde se můžeme podívat na jména jednotlivých hráčů, jejich umístění v závodě, jména AI, která tato hráče ovládala (pokud nějaké byly) a počet tahů, za který byl tento hráč schopen závod dokončit.

## Pravidla hry

Jak již bylo zmíněno v úvodu, cílem hry je v Racetrack dostat se ze startu do cíle a vyhrává ten hráč, kterému se tak povede učinit za nejnižší počet tahů. Start je na mapě označen zelenou vlajkou a cíl je označen vlajkou červenou. Pokud je na mapě cílů více, hráč může dojet do libovolného z nich. Dále jsou zde v některých mapách navíc "checkpointy" označené modrou vlajkou. Více modrých vlajek vedle sebe tvoří dohromady jeden checkpoint a to i v případě, že se jeho jednotlivá políčka dotýkají pouze rohem. Aby hráč dokončil závod na mapě s checkpointy, musí před tím, než dojede do cíle, všechny checkpointy v libovolném pořadí projet. Pokud se hráči nepovede dokončit závod za 500 tahů je diskvalifikován.

Na mapě se kromě startu, cílů a checkpointů nachází samozřejmě i další políčka. Některá z nich nejsou v ničem zvláštní, ale některá mají speciální efekty. Autíčka se "neteleportují" (nelze například "přeskočit" stěnu). To znamená, že autíčko projede v každém kole všechna políčka mezi tím, kde to kolo začíná a tím, kde ho končí. Políčka mezi tím počátečním a koncovým projede přímočarou oboustranně souměrnou trasou, přičemž se může mezi políčky pohybovat i diagonálně. (viz obrázek)

SCREENSHOT-PATHFINDING

Zde je přehled všech typů políček a jak fungují:

- Silnice (černá) - Nemá žádný speciální efekt.

- Tráva (zelená) - Nemá žádný speciální efekt.

- Stěna (cihlová textura) - Po stěně se samozřejmě nedá jezdit. Pokud autíčko do stěny narazí, je zastaveno (jeho vektor pohybu je nastaven na {0,0}) a jako postih za naražení je autíčko na další tři tahy nepojízdné (hráč ovládající toto autíčko další tři tahy nehraje).
						  - Pokud autíčko "narazí" do okraje mapy, je penalizováno stejně, jako při nárazu do stěny.

- Voda (modrá) - Pokud autíčko během své trasy vjede do vody, "potopí se" a nemůže pokračovat v závodě. Hráč je diskvalifikován.

- Písek (žlutá) - Pokud autíčko vjede do písku, je zde zastaveno (nedojede celou trasu svého tahu) a jeho vektor pohybu je nastaven na {0,0}. Tím pádem se při přejíždění více políček písku autíčko může pohybovat jen o jedno políčko za kolo.

- Led (modro-bílá) - Pokud autíčko skončí svůj tah na ledu, dostává se do smyku a v příštím tahu hráč ovládající toto autíčko nemůže měnit jeho vektor pohybu.

## Implementace vlastního AI do hry

Pro přidání vlastního AI do hry je zapotřebí provést tyto kroky:

	1) Napište své AI (samozřejmě v programovacím jazyce Java) jako implementaci rozhraní "DriverAI.java", které naleznete v jar souboru aplikace ve složce "ai\DriverAI.java".

	2) Vložte java soubor svého AI do jar souboru aplikace do složky "ai\VaseAI.java".

	3) Přidejte jméno souboru vašeho AI do souboru "META-INF\ai.txt" v jar souboru aplikace.

	4) Zapněte hru a v nastavení hry přiřaďte vaše AI jednomu z aut.

## Přidání vlastní mapy do hry

Pro přidání vlastní mapy do hry je zapotřebí provést tyto kroky:

	1) Stáhněte si a nainstalujte editor map "Tiled". ([www.mapeditor.org](https://www.mapeditor.org/))

	2) Vytvořte novou mapu v programu Tiled pomocí tile-setu, který najde v jar souboru aplikace ve složce "maps\RacetrackTileSet.tsx".

	3) Vložte tmx soubor vaší mapy do jar souboru aplikace do složky "maps\VaseMapa.tmx".

	4) Přidejte jméno souboru vaší mapy do souboru "META-INF\maps.txt".

	5) Zapněte hru a v nastavení hry zvolte vaší mapu.