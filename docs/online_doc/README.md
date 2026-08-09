# Website

This website is built using [Docusaurus](https://docusaurus.io/), a modern static website generator.

## Installation

```bash
yarn
```

## Local Development

```bash
yarn start
```

This command starts a local development server and opens up a browser window. Most changes are reflected live without having to restart the server.

## Build

```bash
yarn build
```

This command generates static content into the `build` directory and can be served using any static contents hosting service.

## Deployment

Using SSH:

```bash
USE_SSH=true yarn deploy
```

Not using SSH:

```bash
GIT_USER=<Your GitHub username> yarn deploy
```

If you are using GitHub pages for hosting, this command is a convenient way to build the website and push to the `gh-pages` branch.










npx puppeteer browsers install chrome
npx docs-to-pdf --initialDocURLs="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/docs" --contentSelector="article" --paginationSelector=".pagination-nav__link--next" --excludeSelectors=".fixedHeaderContainer,footer.nav-footer,#docsNav,nav.onPageNav,a.edit-page-link,div.docs-prevnext,.sandbox" --coverTitle="Documentation Utilisateur" --coverImage="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/img/logo.png" --disableTOC --outputPDFFilename="Manuel-Utilisateur.pdf"
npx docs-to-pdf --initialDocURLs="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/conception" --contentSelector="article" --paginationSelector=".pagination-nav__link--next" --excludeSelectors=".fixedHeaderContainer,footer.nav-footer,#docsNav,nav.onPageNav,a.edit-page-link,div.docs-prevnext,.sandbox" --coverTitle="Documentation Conception" --coverImage="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/img/logo.png" --disableTOC --outputPDFFilename="Conception.pdf"
npx docs-to-pdf --initialDocURLs="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/validation" --contentSelector="article" --paginationSelector=".pagination-nav__link--next" --excludeSelectors=".fixedHeaderContainer,footer.nav-footer,#docsNav,nav.onPageNav,a.edit-page-link,div.docs-prevnext,.sandbox" --coverTitle="Documentation Validation" --coverImage="https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/img/logo.png" --disableTOC --outputPDFFilename="Validation.pdf"
ln -s ../../javadoc javadoc
sed -i 's/\r$//' decac