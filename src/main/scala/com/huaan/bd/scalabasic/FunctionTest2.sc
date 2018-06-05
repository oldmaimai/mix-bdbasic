Array(2.1, 3, 1) map ((x: Double) => x * 3)
def valueAtOneQuarter(f: (Double) => Double) = f(0.25)
valueAtOneQuarter((x) => x * 4)
// 一个参数可省略括号
valueAtOneQuarter(x => x*5)
// 参数在右侧仅出现一次，可用_替换
valueAtOneQuarter(_ * 6)
// _ 可将方法变成函数(length本来是string的方法)
def fun1 = (_: String).length
fun1("dd")

(1 to 9).map(0.1 * _)
(1 to 9).map(x => 0.1 * x)
(1 to 9).map((x) => 0.1 * x)

// 注意： _代表可以推导出类型的参数
(1 to 9).map("*" * _).foreach(println _)
(1 to 9).filter(_ % 2 == 0)
// 函数中参数在右侧只出现一次才能用_，否则要用x这种参数
(1 to 9).filter(x => x % x == 0)

// reduceLeft需要传入两个参数，所以有2个_
(1 to 9).reduceLeft(_ * _)
// 等价
(1 to 9).reduceLeft((x, y) => x * y)

"i am a chinese".split(" ").sortWith(_.length < _.length)
"i am a chinese".split(" ").sortWith((x, y) => x.length < y.length)

(1.5+12.08)*(1.5+4+5+2.55+3.3+4.16)-(5+2.55+3.3+4.16)*(12.08-4.08+1.5)/2











