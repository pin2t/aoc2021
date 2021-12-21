package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

var re regexp.Regexp = *regexp.MustCompile("\\d")

type tuple struct{ pos1, pos2, score1, score2, player int }

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	s := re.FindAllString(scanner.Text(), -1)
	pos1, _ := strconv.ParseInt(s[1], 0, 0)
	scanner.Scan()
	s = re.FindAllString(scanner.Text(), -1)
	pos2, _ := strconv.ParseInt(s[1], 0, 0)
	pos := []int{int(pos1), int(pos2)}
	score := []int{0, 0}
	dice := 1
	rolls := 0
	roll := func(player int) {
		pos[player] = (pos[player]+dice-1)%10 + 1
		dice++
		if dice > 100 {
			dice -= 100
		}
		rolls++
	}
	for i := 0; score[0] < 1000 && score[1] < 1000; i++ {
		player := i % 2
		roll(player)
		roll(player)
		roll(player)
		score[player] += pos[player]
	}
	if score[0] >= 1000 {
		fmt.Println(score[1] * rolls)
	} else {
		fmt.Println(score[0] * rolls)
	}
	tuples := map[tuple]int64{}
	tuples[tuple{int(pos1), int(pos2), 0, 0, 1}] = 1
	wins := []int64{0, 0}
	splits := []int{1 + 1 + 1, 1 + 1 + 2, 1 + 1 + 3, 1 + 2 + 1, 1 + 2 + 2, 1 + 2 + 3, 1 + 3 + 1, 1 + 3 + 2, 1 + 3 + 3,
		2 + 1 + 1, 2 + 1 + 2, 2 + 1 + 3, 2 + 2 + 1, 2 + 2 + 2, 2 + 2 + 3, 2 + 3 + 1, 2 + 3 + 2, 2 + 3 + 3,
		3 + 1 + 1, 3 + 1 + 2, 3 + 1 + 3, 3 + 2 + 1, 3 + 2 + 2, 3 + 2 + 3, 3 + 3 + 1, 3 + 3 + 2, 3 + 3 + 3}
	for {
		empty := true
		t := tuple{}
		var n int64
		for t, n = range tuples {
			empty = false
			break
		}
		if empty {
			break
		}
		delete(tuples, t)
		for _, split := range splits {
			if t.player == 1 {
				pos := (t.pos1+split-1)%10 + 1
				if t.score1+pos >= 21 {
					wins[0] += n
				} else {
					tuples[tuple{pos, t.pos2, t.score1 + pos, t.score2, 2}] += n
				}
			} else {
				pos := (t.pos2+split-1)%10 + 1
				if t.score2+pos >= 21 {
					wins[1] += n
				} else {
					tuples[tuple{t.pos1, pos, t.score1, t.score2 + pos, 1}] += n
				}
			}
		}
	}
	fmt.Println(maxSlice(wins))
}
