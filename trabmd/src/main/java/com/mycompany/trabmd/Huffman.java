package com.mycompany.trabmd;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// Um nó da árvore
class No
{
	char caractere;
	int freq;
	No esq = null;
        No dir = null;

	No(char caractere, int freq)
	{
		this.caractere = caractere;
		this.freq = freq;
	}

	public No(char caractere, int freq, No esq, No dir) {
		this.caractere = caractere;
		this.freq = freq;
		this.esq = esq;
		this.dir = dir;
	}
};

class Huffman
{
	// percorre a árvore Huffman e armazena códigos Huffman
        // em um mapa.
	public static void codifica(No raiz, String str,Map<Character, String> huffmanCode)
	{
		if (raiz == null)
			return;

		// encontrou uma folha
		if (raiz.esq == null && raiz.dir == null) {
			huffmanCode.put(raiz.caractere, str);
		}


		codifica(raiz.esq, str + "0", huffmanCode);
		codifica(raiz.dir, str + "1", huffmanCode);
	}

	// atravessar a árvore Huffman e decodificar a string codificada
	public static int decodifica(No raiz, int indice, StringBuilder sb)
	{
		if (raiz == null)
			return indice;

		// encontra o codigo de uma folha
		if (raiz.esq == null && raiz.dir == null)
		{
			System.out.print(raiz.caractere);
			return indice;
		}

		indice++;

		if (sb.charAt(indice) == '0')
			indice = decodifica(raiz.esq, indice, sb);
		else
			indice = decodifica(raiz.dir, indice, sb);

		return indice;
	}

	// Construir Árvore Huffman e Código Huffman e codifica os dados fornecidos
	public static void arvoreHuffman(String texto)
	{
		// conta a frequência de aparência de cada letra
                // e armazena-o em um mapa
		Map<Character, Integer> mapa = new HashMap<>();
		for (int i = 0 ; i < texto.length(); i++) {
			if (!mapa.containsKey(texto.charAt(i))) {
				mapa.put(texto.charAt(i), 0);
			}
			mapa.put(texto.charAt(i), mapa.get(texto.charAt(i)) + 1);
		}

		// Crie uma fila de prioridade para armazenar nós ativos da árvore Huffman
                // O item de maior prioridade tem a menor frequência
		PriorityQueue<No> pq = new PriorityQueue<>(
                        (l, r) -> l.freq - r.freq);

		// Crie um nó folha para cada letra e adiciona
                // para a fila de prioridade.
		for (Map.Entry<Character, Integer> entry : mapa.entrySet()) {
			pq.add(new No(entry.getKey(), entry.getValue()));
		}

		// percorre enquanto tiver mais de um nó na fila
		while (pq.size() != 1)
		{
			// Remove os dois nós de maior prioridade
			// frequência mais baixa da fila
			No left = pq.poll();
			No right = pq.poll();

			// Cria um novo nó interno com esses dois nós como filhos
                        // e com frequência igual à soma dos dois nós
                        // Adiciona o novo nó à fila de prioridade.
			int sum = left.freq + right.freq;
			pq.add(new No('\0', sum, left, right));
		}

		// raiz armazena ponteiro para raiz da árvore Huffman
		No raiz = pq.peek();

		// percorre a árvore Huffman e armazenar os códigos Huffman em um mapa
		Map<Character, String> huffmanCode = new HashMap<>();
		codifica(raiz, "", huffmanCode);

		// Imprime o Codigo de Huffman
		System.out.println("Lestras do texto codificadas :\n");
		for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		System.out.println("\nTexto Original :\n" + texto);

		// Imprime a codificação
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < texto.length(); i++) {
			sb.append(huffmanCode.get(texto.charAt(i)));
		}

		System.out.println("\nCodigo de Huffman do Texto :\n" + sb);

		// percorre a árvore Huffman novamente e desta vez
		// decodifica o texto codificado
		int index = -1;
		System.out.println("\nDecodificacao do Codigo: \n");
		while (index < sb.length() - 2) {
			index = decodifica(raiz, index, sb);
		}
	}

	public static void main(String[] args)
	{
		String txt = "Aproveite os pequenos momentos de alegria da vida, pois um dia você olhará para trás e perceberá que esses momentos foram os que fizeram a sua vida feliz.";

		arvoreHuffman(txt);
	}
}