# Smart Pantry Manager

An Android app that Will helps reduce food waste. The user Will record what ingredients they already have at home and the app will use those ingredients to suggest a recipe for the user to cook immediatly without or delaying anytime. shopping will not be required as the recipe only works with the ingredients you currently have available.

## Key rule: strict matching
the recipe will only suggest only if every ingredient is available at the minimum required quantity in the pantry. if one ingredient is missing the recipe will be excluded
## Database choice: SQLite via Room
Room was chosen because it is local, needs no account or
backend, works offline, and gives compile-time checked queries and automatic list updates
through LiveData.

## Features
- Add, edit and delete pantry items (name, quantity, unit, optional expiry date)
- Pantry list screen (RecyclerView with a custom adapter)
- 15 pre-loaded recipes
- Suggested Recipes screen using the strict-matching logic
- Recipe detail screen
- Settings screen

## Setup and run
1. Install Android Studio (a recent stable version).
2. Clone this repository: `git clone <https://github.com/malvinmoses18-dot/smart-pantry-manager>`
3. Open the project folder in Android Studio and let Gradle sync finish.
4. Create or start an emulator (or connect a physical device with USB debugging on).
5. Press **Run**.
