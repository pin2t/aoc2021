#!ruby
depth = depth2 = aim = pos = 0
$stdin.each_line do |line|
    pair = line.split(' ')
    case pair[0]
    when 'forward'
        pos += pair[1].to_i
        depth2 += pair[1].to_i * aim
    when 'up'
        depth -= pair[1].to_i
        aim -= pair[1].to_i
    when 'down'
        depth += pair[1].to_i
        aim += pair[1].to_i
    end
end
puts(pos * depth, pos * depth2)