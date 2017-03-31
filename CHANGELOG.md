## Changelog

The changelog shows which changes are planned and implemented and in which
tag they are considered working. An item will be listed in the form:
- [x] Implemented feature (version)
- [ ] Planned but not implemented feature


### 2017
- [x] Updated Game Controller to Java 1.8 (2017.1)
- [x] Remove support for Protocol Version 7 (2017.1)
- [x] Updated Penalty bytes for Humanoid League (2017.1)
  - HL_BALL_MANIPULATION: 1 (dec) -> 30 (dec)
  - HL_PHYSICAL_CONTACT: 2 (dec) -> 31 (dec)
  - HL_ILLEGAL_ATTACK: 3 (dec) -> 32 (dec)
  - HL_ILLEGAL_DEFENSE: 4 (dec) -> 33 (dec)
  - HL_PICKUP_OR_INCAPABLE: 5 (dec) -> 34 (dec)
  - HL_SERVICE: 6 (dec) -> 35 (dec)
- [x] Moved to Protocol Version 11 (2017.1)
  - Added Secondary State FREE_KICK
  - Added Secondary State PENALTY_KICK
  - Added Secondary State Information to the protocol (4 bytes describing details about the secondary state)
- [x] Penalty Kick during the Game (2017.1)
  - Restricted pressing Finish during PenaltyKick Preparation
- [x] Free Kick during the Game (2017.1)
  - Restricted pressing Finish during a FreeKick Preparation
- [x] Referee Timeout (2017.1)
- [x] Align Adult League (2017.1)
- [ ] Drop in challenge for Humanoid League
