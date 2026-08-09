import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';
import Link from '@docusaurus/Link';


type FeatureItem = {
  title: string;
  link: string;
};

const FeatureList: FeatureItem[] = [
    {
        title: 'Documentation Utilisateur',
        link: '/docs'
    },
    {
        title: 'Documentation Conception',
        link: '/conception'
    },
    {
        title: 'Documentation Validation',
        link: '/validation'
    }
];

function Feature({title, link}: FeatureItem) {
  return (
    <div className={clsx('col')}>
      <div className="text--center padding-horiz--md">
          <Link
              className="button button--secondary button--lg"
              to={link}>
            {title}
        </Link>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): ReactNode {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className={"row " + styles.row}>
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
          <div style={{textAlign: "center", marginTop: "5em"}}>
              Oussama B. | Tonin C. | Clément F. | Richard G. | Nathan G. | Ahmed K. | Abderrahmen L. | Gabriel V.
          </div>
      </div>
    </section>
  );
}
