# LrParser
[![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/hyperium/hyper/master/LICENSE)

## What's LrParser ?
Simple lr parser implementation that respect this rules :

![rules](https://github.com/kassisdion/LrParser/blob/master/media/rule.png?raw=true)
![table](https://github.com/kassisdion/LrParser/blob/master/media/table.png?raw=true)

## Output

It will generate this output :

```gradle
  With id + id * id, I got this output (See the main function):
  
  [0] [ID, PLUS, ID, TIMES, ID, EOF] -> Shift 5
  [0, 5] [PLUS, ID, TIMES, ID, EOF] -> Reduce 6
  [0, 3] [PLUS, ID, TIMES, ID, EOF] -> Reduce 4
  [0, 2] [PLUS, ID, TIMES, ID, EOF] -> Reduce 2
  [0, 1] [PLUS, ID, TIMES, ID, EOF] -> Shift 6
  [0, 1, 6] [ID, TIMES, ID, EOF] -> Shift 5
  [0, 1, 6, 5] [TIMES, ID, EOF] -> Reduce 6
  [0, 1, 6, 3] [TIMES, ID, EOF] -> Reduce 4
  [0, 1, 6, 9] [TIMES, ID, EOF] -> Shift 7
  [0, 1, 6, 9, 7] [ID, EOF] -> Shift 5
  [0, 1, 6, 9, 7, 5] [EOF] -> Reduce 6
  [0, 1, 6, 9, 7, 10] [EOF] -> Reduce 3
  [0, 1, 6, 9] [EOF] -> Reduce 1
  [0, 1] [EOF] -> Accept
```

