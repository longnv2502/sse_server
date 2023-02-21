export class Stack<T> {
  private readonly items: T[]

  constructor(iterable: Iterable<T> = []) {
    this.items = [...iterable]
  }

  pop = () => this.items.pop()
  push = (...items: T[]) => this.items.push(...items)
  peek = () =>
    this.length == 0
      ? undefined
      : this.items[this.length - 1]

  get length() {
    return this.items.length
  }

  *[Symbol.iterator]() {
    for (let item of this.items) yield item
  }
}
