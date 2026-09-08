#Versão do NODE (ponto de referência)
FROM node:24-alpine

#WORKDIR = diretório de trabalho (nosso código)
WORKDIR /app

# Puxa os arquivos .json
COPY package*.json ./ 

# Instala as dependências pro app
RUN npm install

# Copia tudo (arquivos do projeto) em uma imagem pro docker (a receita para o docker baixar tudo nas diferentes máquinas(o que faz o código rodar em diferentes ambientes))
COPY . .

# Porta do localhost
EXPOSE 4200

# Roda o container do frontend
CMD ["npm", "start", "--", "--host", "0.0.0.0"]