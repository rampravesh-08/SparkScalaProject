package com.scala.ds

import scala.collection.mutable.Stack

case class Node(data:Int){
  var l:Node = null
  var r:Node = null  
}

class BST{
  
  def insertNode(root:Node,data:Int): Node={
    if(root == null) return Node(data)
    var tmpRoot,preRoot = root
    while(tmpRoot!=null){
      preRoot=tmpRoot
      tmpRoot = if(tmpRoot.data>data) tmpRoot.l else tmpRoot.r
    }
    if(preRoot.data>data)preRoot.l=Node(data) else preRoot.r=Node(data)
    root
  }
  def inorder(root:Node): Node={
    var stack:Stack[Node] = new Stack()
    if(root == null) return root
    var tmpRoot,preRoot = root
     println("In-Order")
    while(tmpRoot !=null){
      while(tmpRoot != null){
        stack.push(tmpRoot)
        tmpRoot = tmpRoot.l
      }
      while(tmpRoot == null  && !stack.isEmpty){
        val popedItem = stack.pop
        print(popedItem.data +" ")
        tmpRoot = popedItem.r
      }
      
    }
    root
  }
  def preorder(root:Node){
    
    var stack:Stack[Node] = new Stack()
    if(root == null) return root
    var tmpRoot,preRoot = root
    println("\n Pre-Order")
    while(tmpRoot !=null){
      while(tmpRoot != null){
        print(tmpRoot.data+" ")
        stack.push(tmpRoot)
        tmpRoot = tmpRoot.l
      }
      while(tmpRoot == null  && !stack.isEmpty){
        val popedItem = stack.pop
        tmpRoot = popedItem.r
      }
      
    }
    root
  
  }
  def postorder(root:Node): Node={
    var stack:Stack[Node] = new Stack()
    var current = root
    println("\n Pre-Order")
    while(true){
      while(current != null){
        if(current.r!=null)
          stack.push(current.r)
        stack.push(current)
        current = current.l
      }
      if(stack.isEmpty) return root
      current = stack.pop()
      
      if(current.r != null && !stack.isEmpty && current.r == stack.top){
        stack.pop()
        stack.push(current)
        current = current.r
      }else{
        print(current.data +" ")
        current=null
      }
    }
    root
  }

}

object BST {
  def main(args: Array[String]): Unit = {
    val data = List(10,15,8,4,21,7,20,2,6)
    var root:Node = null;
    val bst = new BST()
    for(d <- data){
     root=bst.insertNode(root, d)
    }
    
    bst.inorder(root)
    bst.preorder(root)
    bst.postorder(root)
    
  }
}