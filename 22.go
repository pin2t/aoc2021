package main

import (
	"bufio"
	"fmt"
	"os"
)

type cube struct{ x, y, z int }
type cuboid struct {
	x1, x2, y1, y2, z1, z2 int
	on                     bool
}

func (c cuboid) intersect(with cuboid) bool {
	return mini(c.x2, with.x2) >= maxi(c.x1, with.x1) &&
		mini(c.y2, with.y2) >= maxi(c.y1, with.y1) &&
		mini(c.z2, with.z2) >= maxi(c.z1, with.z1)
}

func (c cuboid) intersection(with cuboid) cuboid {
	return cuboid{maxi(c.x1, with.x1), mini(c.x2, with.x2),
		maxi(c.y1, with.y1), mini(c.y2, with.y2),
		maxi(c.z1, with.z1), mini(c.z2, with.z2), !with.on}
}

func (c cuboid) cubes() int64 {
	return int64(c.x2-c.x1+1) * int64(c.y2-c.y1+1) * int64(c.z2-c.z1+1)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	oncubes := map[cube]bool{}
	restriction := cuboid{-50, 50, -50, 50, -50, 50, false}
	cuboids := make([]cuboid, 0)
	for scanner.Scan() {
		cmd := scanner.Text()
		coords := readInts(cmd)
		c := cuboid{
			int(min(coords[0], coords[1])), int(max(coords[0], coords[1])),
			int(min(coords[2], coords[3])), int(max(coords[2], coords[3])),
			int(min(coords[4], coords[5])), int(max(coords[4], coords[5])),
			cmd[0:2] == "on"}
		// part 1
		if c.intersect(restriction) {
			cc := c.intersection(restriction)
			for x := cc.x1; x <= cc.x2; x++ {
				for y := cc.y1; y <= cc.y2; y++ {
					for z := cc.z1; z <= cc.z2; z++ {
						if cmd[0:2] == "on" {
							oncubes[cube{x, y, z}] = true
						} else {
							delete(oncubes, cube{x, y, z})
						}
					}
				}
			}
		}
		// part 2
		intersections := make([]cuboid, 0)
		for _, cc := range cuboids {
			if c.intersect(cc) {
				intersections = append(intersections, c.intersection(cc))
			}
		}
		cuboids = append(cuboids, intersections...)
		if c.on {
			cuboids = append(cuboids, c)
		}
	}
	fmt.Println(len(oncubes))
	totalcubes := int64(0)
	for _, c := range cuboids {
		if c.on {
			totalcubes += c.cubes()
		} else {
			totalcubes -= c.cubes()
		}
	}
	fmt.Println(totalcubes)
}
