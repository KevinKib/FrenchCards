# Changelog

All notable changes to this project are documented here. This project adheres to [Semantic Versioning](https://semver.org/); while on `0.x` the API may change between minor versions.

## [Unreleased]

### Added
- Deal cards already face down: every `Deck` distribution method now accepts a `DistributionOptions`, and `DistributionOptions.DEFAULT.hidden()` turns each dealt card face down.

### Changed
- **`distributeAllEvenly(nbPlayers)` is now a public method** (replacing the `DistributionOptions.IDENTICAL_CARDS_NUMBER` flag) for dealing the whole deck with an equal split, throwing `UnevenNumberOfCardsPerPlayerException` when it cannot divide evenly.
- `distributeAll(nbPlayers, DistributionOptions)` deals round-robin (uneven split allowed) and no longer declares `throws UnevenNumberOfCardsPerPlayerException`; use `distributeAllEvenly` for the even-split behavior.

### Removed
- `DistributionOptions.IDENTICAL_CARDS_NUMBER` and the record's `isIdenticalCardsNumberForced` flag; `DistributionOptions.DEFAULT` remains. Even distribution is now requested via `distributeAllEvenly`.

## [0.2.0]

This release focuses on making the library genuinely reusable and trimming the card model down to its essentials. Several changes are breaking — expected while pre-1.0.

### Added
- Custom deck types can be registered with `new CardsService(List<DeckFactory>)`; the no-arg constructor still defaults to the French factory.
- Deterministic shuffling: `DeckCreationOptions` accepts an optional seed, so the same seed produces the same deck order (handy for tests, replays, debugging).
- `Card.toString()` renders a readable `"<rank> of <suit>"` (e.g. `ACE of SPADE`).
- Test helpers (`org.kevinkib.cards.testhelpers`) are published as a separate `test-jar` so consumers can opt in for their own tests.

### Changed
- **Card state simplified to a binary `Visibility` (`SHOWN` / `HIDDEN`).** Replaces the `CardState` / `CardHandState` / `CardPileState` hierarchy; the "shown to other players" audience is dropped. `Card` now exposes neutral `show()` / `hide()` mutators instead of container-specific ones.
- **`DeckFactory.generate`** now takes `DeckCreationOptions` instead of `Visibility` (so factories receive the seed). Custom factories need this one-line signature change.
- JUnit and Hamcrest moved to `test` scope — the main artifact no longer drags test dependencies onto a consumer's compile classpath.
- `Pile`'s subscriber-notification methods are now private; only real pile mutations fire events.

### Removed
- Card location tracking (`isInHand()` / `isInPile()` and the `CardPosition` enum); which container a card is in is the container's concern.

### Notes
- Cards remain equal by rank and suit only — by design. Games using multiple decks cannot distinguish duplicate cards through equality. See "Known limitations" in the README.

## [0.1.0]

- Initial release: French deck creation, dealing to hands, pile management, and GitHub Packages publishing.
