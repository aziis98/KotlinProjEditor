package com.aziis98.game1

import java.util.*


// Copyright 2016 Antonio De Lucreziis

// #TEST - SUCCESSFUL - 0.03ms per parse of the following file over 10000 executions
/*
fun main(args: Array<String>) {
    val start = System.nanoTime()
    for (i in 0 .. 1000) {
        XmlParser.parseXml("""
        <window>
            <button on-click="test">
                <a/>
                <b/>
            </button>
            <button on-click="prova">
                <c/>
            </button>
        </window>
        """)
    }
    println("Duration: ${
        ((System.nanoTime() - start) / 1000000)
    }ms")
}
*/

fun xmlSplitter(a: Char, b: Char): Boolean = !(
        ((a.isJavaIdentifierPart() || a == '-') && (b.isJavaIdentifierPart() || b == '-')) ||
                (a.isWhitespace() && b.isWhitespace()) ||
                (a == '\\' && b == '"') ||
                (a == '\\' && b == '\''))

fun parseTokens(source: String, splitter: (Char, Char) -> (Boolean)): LinkedList<String> {
    val sb = StringBuilder()

    val list = LinkedList<String>()

    for (i in 0 .. source.length - 1) {
        sb.append(source[i])

        if (i + 1 == source.length) {
            list += sb.toString()
        } else if (splitter(source[i], source[i + 1])) {
            list += sb.toString()
            sb.setLength(0)
        }

    }

    return list
}


object XmlParser {

    fun parseXml(source: String): XmlNode {
        val tokens = parseTokens(source, ::xmlSplitter)
        val root = XmlNode("root")

        parseNode(tokens, root)

        return root.children.values.first().first
    }

    fun parseNode(tokens: LinkedList<String>, parent: XmlNode): XmlNode {
        tokens.optional { it.isBlank() }
        tokens.assert { it == "<" }
        val node = XmlNode(tokens.pop())

        parseAttributes(tokens, node)

        tokens.optional { it.isBlank() }

        if (tokens.peek() == "/") {
            tokens.assert { it == "/" }
            tokens.assert { it == ">" }

            parent.addChild(node)
            return node
        } else {
            tokens.assert { it == ">" }
        }

        while (true) {
            // println(tokens)

            tokens.optional { it.isBlank() }
            if (tokens[0] == "<" && tokens[1] == "/" && tokens[2] == node.tagName) {

                tokens.assert { it == "<" }
                tokens.assert { it == "/" }
                tokens.assert { it == node.tagName }
                tokens.assert { it == ">" }

                break
            }

            parseNode(tokens, node)
        }

        parent.addChild(node)

        return node
    }

    fun parseAttributes(tokens: LinkedList<String>, node: XmlNode) {
        // println("Parsing Attributes for: ${node.tagName}")
        while (tokens.peek() != "/" && tokens.peek() != ">") {
            tokens.optional { it.isBlank() }

            val attrName = tokens.pop()
            tokens.assert { it == "=" }
            val attrValue = parseString(tokens)

            node.attributes.put(attrName, attrValue)

            if (tokens.peek().isBlank()) tokens.pop()
        }
    }

    fun parseString(tokens: LinkedList<String>): String {
        val sb = StringBuilder()
        tokens.optional { it.isBlank() }
        tokens.assert { it == "\"" }
        while (tokens.peek() != "\"") {
            sb.append(tokens.pop())
        }
        tokens.assert { it == "\"" }
        return sb.toString()
    }

    private fun LinkedList<String>.assert(condition: (String) -> (Boolean)) {
        val z = this.pop()

        if (!condition(z)) {
            throw AssertionError(z)
        }
    }

    private fun LinkedList<String>.optional(condition: (String) -> (Boolean)) {
        if (condition(this.peek())) {
            this.pop()
            // println("optional: '" + this.pop() + "'")
        }
    }

}


class XmlNode(var tagName: String) {
    var attributes = XmlAttributes()

    var parent: XmlNode? = null
    var children = HashMap<String, LinkedList<XmlNode>>()

    fun addChild(node: XmlNode) {
        if (!children.contains(node.tagName)) {
            children.put(node.tagName, LinkedList())
        }

        children[node.tagName]!!.add(node)
    }

    fun toFormattedXml(indentation: Int = 0): String {
        if (children.isEmpty())
            return indentation.toIndent() + "<$tagName${attributes.map { e -> " ${e.key}=\"${e.value}\"" }.joinToString("") }/>"
        else
            return indentation.toIndent() + "<$tagName${attributes.map { e -> " ${e.key}=\"${e.value}\"" }.joinToString("") }>\n" +
                    children.flatMap { entry -> entry.value }.map { "${it.toFormattedXml(indentation + 1)}\n" }.joinToString("") +
                    indentation.toIndent() + "</$tagName>"
    }

    override fun toString(): String = "<$tagName${attributes.map { e -> " ${e.key}=\"${e.value}\"" }.joinToString("") }>\n" +
            children.flatMap { entry -> entry.value }.map { node -> "\t$node\n" }.joinToString("") +
            "</$tagName>"

}

class XmlAttributes : HashMap<String, String>()

private fun Int.toIndent(indentStr: String = "  "): String {
    return (1 .. this).map { indentStr }.joinToString("")
}




























