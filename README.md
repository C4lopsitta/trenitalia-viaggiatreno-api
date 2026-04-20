# ViaggiaTreno API
This Kotlin Multiplatform Library has the goal of giving a "sane" interface to the Trenitalia Viaggiatreno API.

It is currently in development, so it is not to be used in production or stable projects, as it is subject to interface 
changes.

The project is licensed under the [GNU Affero GPL License](COPYING), the [Library Template](https://github.com/Kotlin/multiplatform-library-template)
is licensed under the Apache-2.0 license. Any code inside the `src/` directory is to be considered under the Affero GPL 
license unless otherwise stated.

## Features
- [x] List stations for each Region
- [x] Query station information
- [ ] Query trains for each station
- [ ] Get running train information
- [ ] Translation strings getters
- [ ] Weather information
- [ ] Realtime information
- [ ] Statistics
- [ ] Other (currently) undocumented API endpoints


Contributions are welcome through forks and pull requests.
If you are planning on making a Timetables (or similar) application for Android, consider contributing to [this project of mine](https://github.com/C4lopsitta/rfi-timetable-scraper)
which uses this library.

Note that this library is not affiliated with Trenitalia, RFI, or any Italian Railway Company.
Special thanks to [this unofficial documentation](https://github.com/sabas/trenitalia) of the ViaggiaTreno API.
