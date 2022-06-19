# Vizsgaremek

## Leírás

Életem nagyrészt az asztaliteniszhez kötődik, jelenleg is a Magyar Asztalitenisz Szövetségben dolgozok.  
Vizsgaremeknek a Szövetség nyilvántartó rendszerének egy egyszerűsített változatát választottam.

---

## Felépítés

### Entitás 1

Az `Egyesület (Organization)` entitás a következő attribútumokkal rendelkezik:

* azonosító (`orgId`)
* név (`orgName`) (nem lehet üres)
* cím (`address`) (nem lehet üres)
* kapcsolattartó (`contact`) (nem lehet üres, legalább 2 tagból áll)
* email (`email`) (valid email formátum)
* telefonszám (`telNumber`) (nem kötelező)
* lista az egyesület igazolt játékosairól (`players`)

Végpontok:

| HTTP metódus | Végpont                                | Leírás                                                                                                                                                                                                                                                 |
|--------------|----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET          | `"/api/organizations"`                 | lekérdezi az összes klub nevét és azonosítóját                                                                                                                                                                                                         |
| GET          | `"/api/organizations/{orgId}"`         | lekérdezi egy klub adatait `id` alapján a játékoslistával együtt<br/>ha van a lekérdezésben 'tk=true' paraméter,akkor csak a teljeskörű játékengedéllyel rendelkezőket,<br/>ha 'valid=true', akkor csak az érvényes engedéllyel rendelkezőket listázza |
| POST         | `"/api/organizations"`                 | létrehoz egy új klubot (üres játékoslistával)                                                                                                                                                                                                          |
| POST         | `"/api/organizations/{orgId}"`         | módosítja a klub adatait                                                                                                                                                                                                                               |
| DEL          | `"/api/organizations/{orgId}/{playerId}"` | játékos eltávolítása az egyesületből                                                                                                                                                                                                                   |
| DEL          | `"/api/organizations/{orgId}"`            | egyesület törlése, amit csak akkor lehet, ha nincs leigazolt játékosa                                                                                                                                                                                 |

---

### Entitás 2

A `Játékos (Player)` entitás a következő attribútumokkal rendelkezik:

* játékengedély száma (`playerId`)
* név (`playerName`) (nem lehet üres, legalább 2 tagból áll)
* születési idő (`birthDate`) (nem lehet későbbi az aktuális dátumnál)
* anyja neve (`motherName`) (nem lehet üres, legalább 2 tagból áll)
* aktuális egyesülete (`org`)
* igazolás dátuma (`licenseDate`) (leigazolás/átigazolás dátuma)
* játékengedély érvényessége (`licenseValidityDate`)
* játékengedély típusa (`licenseType`) (ez lehet TELJESKÖRŰ vagy EGYÉNI)

Az `Egyesület` és a `Játékos` entitások között kétirányú, 1-n kapcsolat van.  
Egy egyesületnek több játékosa lehet, egy játékos viszont csak 1 egyesülethez lehet leigazolva.

Végpontok:

| HTTP metódus | Végpont                                   | Leírás                                                                                           |
|--------------|-------------------------------------------|--------------------------------------------------------------------------------------------------|
| GET          | `"/api/players"`                          | lekérdezi az összes játékos nevét+azonosítóját                                                   |
| GET          | `"/api/players/{playerId}"`               | lekérdezi egy játékos adatait `playerId` alapján                                                 |
| POST         | `"/api/players"`                          | létrehoz egy új játékost (klubbal [?org={orgId}] vagy klub nélkül)                               |
| POST         | `"/api/players/{playerId}"`               | érvényesíti a játékengedélyt következő június 30-ig és beállítja a j.e. típusát dátumtól függően |
| POST         | `"/api/organizations/{playerId}/{orgId}"` | át/leigazolás (érvényesíti is a játékengedélyt)                                                  |
| DEL          | `"/api/players/{playerId}"`               | játékos törlése                                                                                  |

A játékengedélyek érvényessége maximum 1 év, minden év június 30-án lejárnak, meg kell újítani.  
Le/átigazolni az év bármely szakaszában lehet, de csak július 1. és december 31. között kap TELJESKÖRŰt (csak ezzel lehet
csapatmérkőzést játszani),  
december 31. után (június 30-ig) csak EGYÉNI versenyengedélyt kaphat.  
Le/átigazolás esetén automatikusan meghosszabbítódik az engedély.   
(A valóságban ennél kicsit bonyolultabb, de nem volt cél az élettel való teljes azonosság)

---

## Technológiai részletek

Háromrétegű alkalmazás, MariaDB adatbázissal, Java Spring backenddel, REST webszolgáltatásokkal.  
Mindkét entitáshoz tartozik egy külön controller és repository, valamint egy közös service.

---
