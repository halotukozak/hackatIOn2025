# HackatIOn 2025 - Dokumentacja
* **Data:** 11-13.04.2025
* **Skład zespołu:** Bartosz Buczek, Berard Gawor, Bartłomiej Kozak, Magdalena Pabisz, Olgierd Smyka, Bartłomiej Stylski
* **Nazwa zespołu:** 4BOM
* **Nazwa projektu:** Roomie

## Opis projektu
Naszym celem było stworzenie aplikacji, pomagającej studentom w znajdowaniu współlokatora do akademika. Inspirowaliśmy się koncepcją wykorzystywaną w popularnych aplikacjach randkowych, takich jak Tinder. Podczas rejestrecji użytkownik wypełnia szczegółowy formularz, aby określić swoje preferencje i cechy osobowości. Pomaga to zapewnić lepsze dopasowanie między potencjalnymi współlokatorami. Formularz obejmuje następujące aspekty:
### Informacje o użytkowniku
* harmonogram snu (godzina zasypiania i wstawanie)
* zainteresowania i hobby (wybierane z listy)
* palenie papierosów (tak/nie/okazjonalnie)
* picie alkoholu (tak/nie/okazjonalnie)
* typ osobowości (skala: introwertyk – ekstrawertyk)
* rok studiów
* wydział (wybór z listy)
* status związku (tak/nie/to skomplikowane)
### Preferencje
* harmonogram snu (ma znaczenie lub nie)
* zainteresowania i hobby (ma znaczenie lub nie)
* palenie papierosów (tak/nie/okazjonalnie)
* picie alkoholu (tak/nie/okazjonalnie)
* typ osobowości (skala: introwertyk – ekstrawertyk)
* rok studiów (ma znaczenie lub nie)
* wydział (ma znaczenie lub nie)
* status związku (ma znaczenie lub nie)

Użytkownik może przeglądać profile innych osób, które zawierają kluczowe informację i wartość zgodności dopasowanie. Pierwotny pomysł zakładał interfejs użytkownika w styku podobnym do aplikacji randkowej, gdzie przesunięcie w prawo to nawiązanie interakcji z daną osobą, a przesunięcie w lewo oznacza pominięcie profilu. Pomysłu tego nie udało się jednak zrealizować. W aktualnej wersji dostępne są przyciski o takiej samej funkcjonalności. \
Gdy dwie osoby wzajemnie wyrażą chęć zamieszkania ze sobą, udostępniane są ich dane kontaktowe, umożliwiając dalszą interakcję i rozmowę (poza naszą aplikają). Użytkownik mogże również w każdej chwili usunąć się z systemu, jeśli nie jest już zainteresowany poszukiwaniem współlokatora.

### Kluczowe funkcje aplikacji
- rejestracja i logowanie
- szczegółowy formularz startowy zbierający informacje o preferencjach i cechach użytkownika
- system oceniający dopasowanie potencjalnych współlokatorów
- możliwość wysyłania zapytań do potencjalnych współlokatorów
- udostępnaianie danych kontaktowych w przypadku zainteresowania dwóch osób
- podgląd powiadomień
- podgląd danych użytkownika (swoich i innych osób)

### Algorytm sortujący
Aplikacja wykorzystuje funkcję kosztu do obliczania wartości dopasowania użytkowników. Algortym analizuje różnice w preferencjach i cechach, przypisując wagę każdemu aspektowi w zależności od opcji zaznaczonych przez użytkownika. Na przykład:\
Mniejsze różnice w harmonogramach snu powodują lepsze dopasowanie.\
Większe różnice w kwestii palenia lub picia alkoholu powodują mniejszą wartość dopasowania.\
Profile potencjalnych współlokatorów sortowane są według obliczonej wartości dopasowania. W pierwszej kolejności prezentowane są osoby o najwięszym stopniu zgodności.


## Zastosowane technologie

### Backend

- **Kotlin**  
  Główny język używany do programowania backendu. W projekcie wykorzystujemy `context receivers` do zarządzania zależnościami w sposób idiomatyczny i przejrzysty.

- **Ktor**  
  Asynchroniczny framework webowy stworzony używany jako główny serwer HTTP, obsługuje routing, autoryzację oraz middleware.

- **PostgreSQL**  
  Stabilna i wydajna relacyjna baza danych, wykorzystywana do przechowywania danych aplikacji.

- **Exposed**  
  Kotlinowy framework ORM do pracy z bazą danych. Korzystamy zarówno z DSL (Domain Specific Language), jak i DAO (Data Access Objects) w celu wygodnego i typowanego zarządzania encjami.

- **Testcontainers**  
  Biblioteka umożliwiająca uruchamianie izolowanych instancji PostgreSQL w kontenerach Dockerowych w trakcie testów integracyjnych.

- **Docker + Render**  
  Deployment aplikacji realizowany w podejściu *Infrastructure as Code* z użyciem kontenerów Dockerowych oraz platformy Render do hostowania backendu.

- **Eksport modeli REST do TypeScript**  
  Automatyczne generowanie typów TypeScript z modeli Kotlinowych w celu zapewnienia spójności danych między backendem a frontendem.

- **Swagger / OpenAPI**  
  Backend automatycznie generuje dokumentację API w formacie OpenAPI, udostępnianą w formie interaktywnego Swagger UI.

- **Faker**  
  Biblioteka służąca do generowania losowych danych wykorzystywanych podczas testowania i developmentu.
  
### Frontend

- **React + TypeScript**  
  React jako biblioteka do budowy interfejsu użytkownika, w połączeniu z TypeScriptem zapewnia typowanie i lepsze bezpieczeństwo kodu.

- **Tailwind CSS**  
  Framework CSS, umożliwiający szybkie tworzenie nowoczesnych i responsywnych interfejsów użytkownika bez potrzeby pisania własnych klas CSS.

- **DaisyUI**  
  Biblioteka komponentów UI zbudowana na Tailwind CSS, dostarczająca gotowe do użycia komponenty.

- **v0**  
  Narzędzie do generowania makiet UI i inspiracji wizualnych, które przyspieszyło proces projektowania interfejsu.


## Uruchamianie aplikacji TODO
- instrukcję uruchomienia aplikacji - będziemy chcieli to zweryfikować u siebie i może to wpłynąć na ocenę za efekt końcowy, więc sprawdźcie czy faktycznie działa!


Przed rozpoczęciem upewnij się, że masz zainstalowany Docker oraz że usługa Docker Daemon jest uruchomiona. Szczegółowe instrukcje znajdziesz w oficjalnej dokumentacji: [Uruchamianie Docker Daemon](https://docs.docker.com/engine/daemon/start/).
#### Uruchamianie w kontenerach
1. **Backend**:  
   ```bash
   cd backend
   docker build -t backend-app .
   docker run -p 8080:8080 backend-app
   ```
2. **Frontend**:  
   ```bash
   cd frontend
   docker build -t frontend-app .
   docker run -p 5173:5173 frontend-app
   ```

#### Uruchamianie lokalnie
1. **Backend**:  
   ```bash
   cd backend
   ./gradlew build
   ./gradlew run
   ```
2. **Frontend**:  
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

#### Dostęp do aplikacji
Po uruchomieniu aplikacji frontendowej, otwórz przeglądarkę i przejdź pod adres:  
[http://localhost:5173/](http://localhost:5173/)  
Tam znajdziesz działającą aplikację webową.



## Proces powstawania projektu TODO
- opis procesu powstawania projektu - może być to w formie np. "dziennika wydarzeń", grunt by opisywało chronologicznie Wasze doświadczenia
### Piątek (11.04)
- określenie tematu i funkcjonalności aplikacji
- podział na podzespoły (frontend/backend)
- inicjalizacja repozytorium, projektów
### Sobota (12.04)
- **(Frontend)** zaprojektowanie makiet UI
- **(Frontend)** przygotowanie formularza do logowania i rejestracji

- **(Frontend)** widok listy potencjalnych współlokatorów

- **(Frontend)** zaprojektowanie formularza do zbierania preferencji i cech osobowości (pierwsze połączenie z backendem - lista hobby i dostępnych wydziałów)
- **(Frontend)** widok listy powiadomień
- **(Frontend)** widok profilu użytkownika
- **(Backend)** Integracja z in-memory bazą danych H2
- **(Backend)** Implementacja systemu oraz endpointów obsługujących rejestrację i logowanie użytkowników.
- **(Backend)** Opracowanie modelu danych przechowującego informacje o preferencjach oraz szczegółach użytkowników.
- **(Backend)** Wdrożenie systemu autentykacji użytkowników, zapewniającego bezpieczeństwo dostępu.
- **(Backend)** Stworzenie endpointów zwracających listę dostępnych wydziałów oraz hobby do wyboru.

### Niedziela (13.04)
- **(Frontend)** umożliwienie logowanie użytkownika
- **(Frontend)** wyświetlanie informacji o użykowniku pobranej z backendu
- **(Frontend)** wyświetlanie listy kandydatów pobranej z backendu
- **(Frontend)** umożliwienie rejestracji uzytkownika (wysłanie informacji na backend)
- **(Frontend)** wyświetlanie powiadomień pobranych z backendu
- **(Frontend)** dodanie funkcjonalności zaproszenia i odrzucenia potencjalnego współlokatora (wysyłanie informacji na backend)
- **(Frontend)** możliwość wylogowania użytkownika
- **(Backend)** integracja modelu danych z danymi przesyłanymi przez frontend, zapewniająca spójność i poprawność informacji.
- **(Backend)** implementacja endpointów umożliwiających dodawanie informacji o użytkownikach oraz ich preferencji.
- **(Backend)** stworzenie mechanizmu oraz endpointów odpowiedzialnych za dopasowywanie użytkowników na podstawie ich preferencji.
- **(Backend)** implementacja endpointów obsługujących powiadomienia o wychodzących i przychodzących dopasowaniach.
- **(Backend)** opracowanie algorytmu obliczającego metrykę podobieństwa między użytkownikami, uwzględniającego ich preferencje i cechy.
- **(Backend)** Deployment i integracja z bazą danych PostgreSQL


## Wyzwania i probelmy TODO
(główne wyzwania i problemy oraz jak sobie z nimi poradziliście (można wykorzystać wnioski z retrospektywy))
1. **Trudności w nauce nowych technologi**
Niektóre osoby początkowo miały problemy z przyswojeniem nowych narzędzi. Między innymi, mało kto w zespole miał doświadczenie z technologiami frontendowymi. Udało nam się jednak sprawnie opanować porzeba narzędzia, a osoby bardziej doświadczone technicznie potrafiły skutecznie pokierować pracami i doradzić gdy osoby potrafiące mniej miały problemy.
2. **Problemy komunikacyjne między frontendem a backendem**  
   Brak jasnych ustaleń dotyczących kontraktów API i niedoprecyzowane wymagania skutkowały opóźnieniami i nieporozumieniami. Rozwiązanie polegało na oddelegowaniu dwóch osób (jedej z backendu i jedej z frontendu), aby wspólnie ustaliły potrzebne informacje.
3. **Nierównomierne tempo prac – backend wolniejszy niż frontend**  
   Z powodu większej złożoności oraz problemów z wdrożeniem technologii backendowych, ta część rozwijała się wolniej.
4. **Rozwiązywanie merge conflictów**
W trakcie rozwoju aplikacji pojawiały się konflikty, staraliśmy się więc nie dopuszczać do sytuacji, w której dwie osoby wprowadzają duże modyfikacje w tym samym pliku. Pozwoliło nam to zaoszczędzić czas na rozwiązywaniu potencjalnych konflików.

## Dokumentacja techniczna
- elementy technicznej dokumentacji - np. architektura systemu, opis komponentów czy zastosowanych pomysłów/wzorców. Warto umieścić tu diagramy (np. klas, przepływu sterowania)

## 

- można też wykorzytać metryki zgromadzone przez githuba (zakłada Insights i wkleić screeny + komentarz do nich.
