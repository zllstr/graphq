import nltk
from nltk.corpus import wordnet
def get_synset_words(word):
    synsets=wordnet.synsets(word)
    words=[syn.name().split(".")[0] for syn in synsets]
    # if word=="designer":
    #     print(words)
  #  print(set(words))
    return set(words)
def get_derivationally_related_forms(word):
    words = [one.name() for syn in wordnet.synsets(word) for lemma in syn.lemmas() for one in lemma.derivationally_related_forms()]
   # print(set(words))
    return set(words)
def get_synset_lemmawords(word):
    synsets=wordnet.synsets(word)
    words=[lemma_name for syn in synsets for lemma_name in syn.lemma_names()]
    print(set(words))
    return set(words)
#get_synset_words("productions")
#get_synset_lemmawords("designer")
print(get_derivationally_related_forms("product"))
# print(wordnet.synsets("productions"))
# a=wordnet.synsets("productions")[0]
# print(a.lemmas())
# print(a.name())
# print(a.lemma_names())
# print(type(a))
#print(a.lemma().derivationally_related_forms())

# vocal = wordnet.lemma('designer.n.01.designer')
# print(vocal.derivationally_related_forms())
# print(vocal.pertainyms())
# print(vocal.antonyms())



#print(a.pos)
#下面的东西没有什么用
# a = wordnet.synsets('invention')[0]
# b = wordnet.synsets('invent')[0]
# a.similar_tos()
# print(a.similar_tos())