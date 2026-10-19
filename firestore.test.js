const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// 1. Unauthenticated access rejected
test("Unauthenticated user: cannot read user profiles or saved items", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("saved_items").get());
});

// 2. Cross-user isolation
test("Cross-user isolation: Bob cannot read Alice's profile or saved items", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      createdAt: new Date(),
    });
    await context.firestore().collection("users").doc(ALICE_UID).collection("saved_items").doc("item_1").set({
      id: "item_1",
      userId: ALICE_UID,
      toolId: "content_writer",
      toolTitle: "كاتب المحتوى",
      inputPrompt: "أفكار مقالات",
      outputContent: "1. الذكاء الاصطناعي",
      isFavorite: false,
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("saved_items").doc("item_1").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("saved_items").get());
});

// 3. Authenticated owner can create and read their own items
test("Owner access: Alice can create and read her own profile and saved items", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();

  // Create profile
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      createdAt: new Date(),
    })
  );

  // Read profile
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());

  // Create saved item
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_items").doc("item_alice").set({
      userId: ALICE_UID,
      toolId: "code_assistant",
      toolTitle: "مساعد البرمجة",
      inputPrompt: "كود بايثون",
      outputContent: "print('hello')",
      isFavorite: true,
      createdAt: new Date(),
    })
  );

  // Read saved item
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("saved_items").doc("item_alice").get());
  // List saved items
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("saved_items").get());
});
