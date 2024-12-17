# comixed-metadata-marvel

This project provides a metadata adaptor for the [ComiXed](http://www.comixedproject.org)
digital comic book management system that uses [Marvel's online portal](https://developer.marvel.com/) as
its data source.

# Status

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=comixed_comixed-metadata-marvel)](https://sonarcloud.io/dashboard?id=comixed_comixed-metadata-marvel)


# Limitations:

The following are things to keep in mind when using this metadata scraper:

## Marvel Provides Less Data Than ComicVine

Marvel's database only returns characters and credits for comics. They don't currently return details like teams,
locations, or story arcs for issues. 
 
## The Series Name Must Be A Match

The online database does not do loose name searches; i.e., when searching for a volume, you have to use at least the 
start of the actual name in the database. It does exact matches for the start of the series name.

So, for example, ComicVine has this series name:

    Ghost Rider / Wolverine: Weapons of Vengeance – Alpha 

However, in Marvel's database, this text won't be found. Instead, you would need to use either:

    Ghost Rider/Wolverine: Weapons of Vengeance

or:

    Ghost Rider/Wolverine: Weapons of Vengeance Alpha

The first would return all series that start with that text (currently three titles), while the latter would return
only the one series whose name matches it exactly.
