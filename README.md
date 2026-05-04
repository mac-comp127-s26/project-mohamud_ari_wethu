# Environmental Awareness Mode: An Interactive Single-Player Game

"Shoot Smart, Save the Planet!"

Environmental Awareness Mode is a single-player shooting game where players must carefully choose which targets to shoot. Targets represent real-world environmental elements — harmful items like pollution or trash should be hit for points, while beneficial elements like life on Earth should be avoided or the player loses lives.

![Alt text](res/CompSciGame.jpeg)

## How to Play

- **Move** your mouse to aim the gun
- **Click** to shoot

## Targets

| Target | Shape | Effect |
|--------|-------|--------|
| Harmful (red circle) | Circle | +10 points, destroyed on hit |
| Life on Earth (blue rectangle) | Rectangle | −5 points, loses a life, **stays on screen** |
| Extra Life (green circle) | Circle | +1 life, destroyed on hit |

## Goal

Shoot all **harmful targets** (red circles) to win the wave. Avoid hitting the blue Life on Earth rectangles — they won't go away and each hit costs you a life.

## Difficulty

- Each wave adds more harmful targets and starts them faster (+25% per wave)
- All targets speed up every ~10 seconds within a wave (shown as Speed Level in the bottom right)
- Run out of lives and it's game over

## Controls

| Key | Action |
|-----|--------|
| Mouse click | Shoot |
| Y (after win/game over) | Next wave / Restart |
| N (after win/game over) | Quit |
