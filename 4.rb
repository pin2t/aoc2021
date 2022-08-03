#!ruby
class Board
    def initialize(numbers)
        @numbers = numbers
    end

    def self.parse(input)
        numbers = []
        5.times do 
            numbers.append(input.gets.strip.split(" ").map(&:to_i))
        end
        return Board.new(numbers)
    end

    def mark(n)
        @numbers.each_index do |row|
            @numbers[row].each_index do |col|
                if @numbers[row][col] == n then @numbers[row][col] = -1; end
            end
        end
    end

    def win?
        5.times do |i|
            return true if @numbers[i].all? do |n| n == -1 end
            return true if @numbers.all? do |row| row[i] == -1 end
        end
        return false
    end

    def score(n)
        return n * (@numbers.map do |row| row.sum do |i| [0,i].max end end.inject(&:+))
    end
end
numbers = $stdin.gets.strip.split(",").map(&:to_i)
boards = []
while $stdin.gets != nil do boards.append(Board.parse($stdin)) end
score = -1
numbers.each do |n|
    boards.each do |board|
        next if board.win?
        board.mark(n)
        if board.win?
            if score == -1 then puts(board.score(n)) end
            score = board.score(n)
        end
    end 
end
puts(score)