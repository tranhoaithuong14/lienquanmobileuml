# Mission: AoV Targeting Simulation in Java

## Why

Building a Java teaching artifact that simulates Arena of Valor / Liên Quân Mobile combat systems — starting with the **targeting controls** players actually use in game. Strategy is still useful, but only inside the targeting pipeline; the player/control setting and action context decide which strategy applies.

End goal: read combat-targeting code in Java fluently, recognize where Strategy should and should not be applied in real game code, and write clean implementations with clear UML diagrams for the teaching artifact.

## Success looks like

- Read any Java code that uses the Strategy pattern and immediately recognize the Context, Strategy, and ConcreteStrategy roles — without reading comments.
- Model AoV targeting priority correctly: nearest, lowest HP amount, lowest HP percent, avatar lock, target kind filters, tap-to-cast abilities, directional tap priority, and finisher overrides.
- Explain why Strategy beats `if/else` chains or `switch` statements in this case, and why it beats inheritance.
- Draw a class diagram (PlantUML) for Strategy and apply it to a different subsystem outside target selection.

## Constraints

- **Communication language**: English (the user prefers it for the technical surface). Technical terms stay in English.
- **Code language**: Java (locked in at the previous grilling session).
- **UML tool**: PlantUML (locked in at the previous grilling session).
- Each lesson must be short — working memory is tiny. One clear win per lesson.
- Code samples use Java 17+ features (records, switch expressions) if they make examples tighter, but not required — prefer classic GoF syntax.
- Each lesson must include retrieval practice (quiz) to build storage strength, not just fluency.

## Out of scope

- Other GoF patterns (State, Observer, Command, …) — wait until Strategy is solid.
- Full MOBA scope (140 heroes, ranked, monetization, networking) — the teaching artifact uses the genre as interaction context, not as the end goal.
- Advanced UML (sequence diagrams, state machines) — defer until class diagrams are solid.
- Deep reading of the 1994 GoF book — enough to recognize it as a primary source, not cover-to-cover.

## Open questions

- User's Java/OOP background is unknown (new to Java, or already familiar but not with patterns). Needs to be confirmed in the first lesson to calibrate.
- The teaching context uses English now; whether the user is comfortable reading English technical docs (Refactoring Guru, GoF book) needs a check. If not, Vietnamese resources needed.
