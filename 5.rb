#!ruby
class Board
    def initialize
        @points = {}
    end

    def mark(x1, y1, x2, y2)
        dx = x1 == x2 ? 0 : (x1 > x2 ? -1 : 1)
        dy = y1 == y2 ? 0 : (y1 > y2 ? -1 : 1)
        x, y = x1, y1
        loop do
            if !@points.key?([x, y]) then @points[[x, y]] = 0 end
            @points[[x,y]] += 1
            if x == x2 && y == y2 then break end
            x, y = x + dx, y + dy
        end
    end

    def points
        return @points.each_value.filter do |v| v > 1 end.inject(&:+)
    end
end
board, board2 = Board.new, Board.new
$stdin.each_line do |line|
    coords = line.strip.scan(/\d+/).map(&:to_i)
    if coords[0] == coords[2] || coords[1] == coords[3]
        board.mark(coords[0], coords[1], coords[2], coords[3])
        board2.mark(coords[0], coords[1], coords[2], coords[3])
    end
    if (coords[2] - coords[0]).abs == (coords[3] - coords[1]).abs
        board2.mark(coords[0], coords[1], coords[2], coords[3])
    end
end
puts(board.points, board2.points)