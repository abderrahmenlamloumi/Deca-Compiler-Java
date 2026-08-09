// @ts-ignore
import {themes as prismThemes} from 'prism-react-renderer';
// @ts-ignore
import type {Config} from '@docusaurus/types';
// @ts-ignore
import type * as Preset from '@docusaurus/preset-classic';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const config: Config = {
  title: 'GL10 Documentation',
  tagline: 'Documentation du projet de compilateur Deca du groupe GL10',
  favicon: 'img/logo.png',

  // Future flags, see https://docusaurus.io/docs/api/docusaurus-config#future
  future: {
    v4: true, // Improve compatibility with the upcoming Docusaurus v4
  },

  // Set the production url of your site here
  url: 'https://abderrahmenlamloumi.github.io',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/Deca-Compiler-Java/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'abderrahmenlamloumi', // Usually your GitHub org/user name.
  projectName: 'Deca-Compiler-Java', // Usually your repo name.

  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'fr',
    locales: ['fr'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          routeBasePath: 'docs',
          path: 'docs',
          sidebarPath: './sidebars.ts',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  plugins: [
      [
        '@docusaurus/plugin-content-docs',
        {
          id: 'conception',
          path: 'conception',
          routeBasePath: 'conception',
          sidebarPath: './sidebars.ts',
          remarkPlugins: [remarkMath],
          rehypePlugins: [rehypeKatex],
          // ... other options
        },
      ],
      [
        '@docusaurus/plugin-content-docs',
        {
          id: 'validation',
          path: 'validation',
          routeBasePath: 'validation',
          sidebarPath: './sidebars.ts',
          // ... other options
        },
      ],
  ],

  markdown: {
    mermaid: true,
  },

  themes: ['@docusaurus/theme-mermaid'],

  themeConfig: {
    // Replace with your project's social card
    image: 'img/logo.png',
    navbar: {
      title: 'GL10 Doc',
      logo: {
        alt: 'My Site Logo',
        src: 'img/logo.png',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'tutorialSidebar',
          position: 'left',
          label: 'Documentation Utilisateur',
        },
        {
          type: 'docSidebar',
          sidebarId: 'tutorialSidebar',
          position: 'left',
          label: 'Documentation Conception',
          docsPluginId: "conception"
        },
        {
          type: 'docSidebar',
          sidebarId: 'tutorialSidebar',
          position: 'left',
          label: 'Documentation Validation',
          docsPluginId: "validation"
        },
        {
          href: 'https://gl.glrm.fr/javadoc/index.html',
          label: 'JavaDoc',
          position: 'left',
        },

      ],
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['java'],
    },
  } satisfies Preset.ThemeConfig,

  stylesheets: [
    {
      href: 'katex.min.css',
      type: 'text/css',
    },
  ],
};

export default config;
