# Releasing

How to cut a release of FrenchCards and publish it to GitHub Packages.

## Prerequisites (one-time setup)

1. **JDK 17** and **Maven** installed.
2. **A GitHub Personal Access Token (classic)** with these scopes:
   - `write:packages` — required to publish
   - `read:packages` — required to resolve published artifacts
   - `repo` — required because the package is tied to a repository

   > A plain `repo` token (or the default `gh` OAuth token) is **not** enough — publishing fails with `401 Unauthorized` unless `write:packages` is present. Check an existing token's scopes with `gh auth status`.

3. **`~/.m2/settings.xml`** must contain a `server` whose `id` matches the
   `distributionManagement` repository id in `pom.xml` (`github`):

   ```xml
   <settings>
     <servers>
       <server>
         <id>github</id>
         <username>KevinKib</username>
         <password>TOKEN_WITH_write:packages</password>
       </server>
     </servers>
   </settings>
   ```

## Release steps

### 1. Make sure `main` is ready
All intended PRs merged, working tree clean:

```bash
git checkout main
git pull
```

### 2. Bump the version
Update the version in all three places (keep them in sync):
- `pom.xml` → `<version>`
- `README.md` → both dependency snippets (main artifact **and** the `test-jar`)
- `CHANGELOG.md` → add a section for the new version

Land this through a PR (e.g. a `release-X.Y.Z` branch), as with any change.

### 3. Build and test
```bash
mvn clean verify
```
This compiles, runs the full test suite, and packages both the main jar and
the test-jar (`maven-jar-plugin`).

### 4. Deploy to GitHub Packages
From `main`, after the version bump is merged:

```bash
mvn deploy
```
This uploads the main jar, the `test-jar`, the POM, and checksums to
`https://maven.pkg.github.com/KevinKib/FrenchCards`. Use `mvn -DskipTests deploy`
only if you have already run the tests in step 3.

### 5. Tag and create the GitHub release
```bash
git tag v0.2.0
git push origin v0.2.0
gh release create v0.2.0 --title "0.2.0" --notes-file <(sed -n '/## \[0.2.0\]/,/## \[0.1.0\]/p' CHANGELOG.md)
```
(Or paste the changelog section into the release notes manually.)

## Verifying the publish
The published versions appear under the repository's **Packages** section on
GitHub, or via:

```bash
mvn dependency:get -Dartifact=org.kevinkib:frenchcards:0.2.0
```
(Reading also requires a token with `read:packages` in `settings.xml`.)

## Troubleshooting

| Symptom | Cause | Fix |
| --- | --- | --- |
| `401 Unauthorized` on deploy | Token missing `write:packages` | Regenerate the PAT with `write:packages` (see Prerequisites) |
| `401` when *consuming* the package | `settings.xml` token missing `read:packages`, or no `server` entry | Add a `github` server with a `read:packages` token |
| `Return code is: 409 Conflict` | Version already published (GitHub Packages is immutable) | Bump to a new version; you can't overwrite an existing one |
| `Could not find artifact ... distributionManagement` | `server` id ≠ `distributionManagement` id | Make both `github` |
