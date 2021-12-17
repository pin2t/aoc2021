package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type point struct{ x, y int64 }

func (p *point) move(v velocity) {
	p.x += v.dx
	p.y += v.dy
}

type velocity struct{ dx, dy int64 }

func (v *velocity) adjust() {
	v.dx--
	if v.dx < 0 {
		v.dx = 0
	}
	v.dy--
}

type target struct{ fromx, tox, fromy, toy int64 }

func (t target) hit(p point) bool {
	return p.x >= min(t.fromx, t.tox) && p.x <= max(t.fromx, t.tox) && p.y >= min(t.fromy, t.toy) && p.y <= max(t.fromy, t.toy)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	re := regexp.MustCompile("\\-?\\d+")
	matches := re.FindAllString(scanner.Text(), -1)
	target := target{}
	target.fromx, _ = strconv.ParseInt(matches[0], 0, 0)
	target.tox, _ = strconv.ParseInt(matches[1], 0, 0)
	target.fromy, _ = strconv.ParseInt(matches[2], 0, 0)
	target.toy, _ = strconv.ParseInt(matches[3], 0, 0)
	highest := int64(0)
	hitvels := 0
	v := velocity{}
	for v.dx = int64(1); v.dx <= max(target.fromx, target.tox); v.dx++ {
		for v.dy = int64(-100); v.dy <= int64(100); v.dy++ {
			p := point{int64(0), int64(0)}
			vv := v
			highy := int64(0)
			for p.x <= max(target.fromx, target.tox) && p.y >= min(target.fromy, target.toy) && !target.hit(p) {
				p.move(vv)
				vv.adjust()
				if p.y > highy {
					highy = p.y
				}
				if target.hit(p) {
					hitvels++
				}
			}
			if target.hit(p) && highy > highest {
				highest = highy
			}
		}
	}
	fmt.Println(highest, hitvels)
}
