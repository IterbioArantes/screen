package learning.com.br.screenMatch.services;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.stereotype.Service;

@Service
public class ConsultaChatGPT {

    private final OpenAIClient client;

    public ConsultaChatGPT(OpenAIClient client) {
        this.client = client;
    }

    public String obterTraducao(String texto){

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("Traduza para o português o texto: " + texto)
                .model(ChatModel.GPT_5_2)
                .maxCompletionTokens(texto.length() * 2L)
                .temperature(0.2)
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);

        return chatCompletion.choices()
                .get(0)
                .message()
                .content()
                .orElse("");


    }
}